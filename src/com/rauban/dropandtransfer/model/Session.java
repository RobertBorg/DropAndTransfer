package com.rauban.dropandtransfer.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.Chat;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.FileData;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.Packet;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.Packet.Type;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferCancel;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferResponse;
import com.rauban.dropandtransfer.util.FileUtil;
import com.rauban.dropandtransfer.view.listener.SessionListener;
import com.rauban.speaker_listener_pattern.listener.Listener;
import com.rauban.speaker_listener_pattern.speaker.AudienceHolder;
import com.rauban.speaker_listener_pattern.speaker.Speaker;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

public class Session implements Runnable, Speaker<SessionListener>, SessionListener {
	private Socket socket;
	private AudienceHolder audience;
	private CodedOutputStream cos;
	private CodedInputStream cis;
	
	private BufferedOutputStream bos;
	private Semaphore outStreamSem;

	private BufferedInputStream bis;
	
	long nextOutGoingTransferOfferId;
	private HashMap<Long, TransferOffer> incomingOfferMap;
	private HashMap<Long, TransferOffer> outgoingOfferMap;
	private HashMap<Long, String> basePathsForOutGoingOffers;
	
	private HashMap<Long, FileTransfer> activeFileTransfers;
	
	public Session(Socket socket) {
		init(socket);
	}
	public Session(InetAddress address, int port) throws IOException {
		init(new Socket(address, port));
	}
	private void init(Socket s) {
		this.socket = s;
		outStreamSem = new Semaphore(1, true);
		audience = new AudienceHolder();
		incomingOfferMap = new HashMap<Long, TransferOffer>();
		outgoingOfferMap = new HashMap<Long, TransferOffer>();
		activeFileTransfers = new HashMap<Long, FileTransfer>();
		basePathsForOutGoingOffers = new HashMap<Long, String>();
	}
	private void cleanUp() {
		try {
			bos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!socket.isClosed()) {
			
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sessionDisconnected();
	}
	public boolean sendTransferResponse(long offerId, boolean accept) {
		TransferResponse.Builder b = TransferResponse.newBuilder();
		b.setAccept(accept);
		b.setOfferId(offerId);
		TransferResponse tr = b.build();	
		TransferOffer to = incomingOfferMap.remove(tr.getOfferId());

		Packet.Builder p = Packet.newBuilder();
		p.setType(Type.RESPONSE);
		p.setTransferResponse(tr);
		sendPacket(p.build());
		
		if(tr.getAccept()) {
			FileTransfer ft = new FileTransfer(to, new File("./Received/"), true);
			activeFileTransfers.put(to.getOfferId(), ft);
			sessionFileTransferStarted(ft);
			incomingOfferMap.remove(to.getOfferId());
		}
		return true;
	}

	public TransferOffer createTransferOffer(File[] resources) {
		if(resources == null || resources.length == 0)
			return null;

		TransferOffer.Builder tob = TransferOffer.newBuilder();
		tob.setOfferId(nextOutGoingTransferOfferId++);
		String base = resources[0].getParentFile().getAbsolutePath();
		FileUtil.iterateResources(tob, resources, base);
		TransferOffer to = tob.build();
		basePathsForOutGoingOffers.put(to.getOfferId(), base);
		return to;
	}
	public void sendCancelRequest(long offerId) {
		Packet.Builder pb = Packet.newBuilder();
		pb.setType(Type.CANCEL);
		TransferCancel.Builder tc = pb.getCancelBuilder();
		tc.setOfferId(offerId);
		sendPacket(pb.build());
		FileTransfer ft = activeFileTransfers.get(offerId);
		ft.cancel();
	}

	public void sendTransferOffer(TransferOffer to) {
		Packet.Builder pb = Packet.newBuilder();
		pb.setType(Type.OFFER);
		pb.setTransferOffer(to);
		outgoingOfferMap.put(to.getOfferId(), to);
		sendPacket(pb.build());
	}

	private void sendPacket(Packet p) {
		boolean got = false;
		try {
			outStreamSem.acquire();
			got = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			p.writeDelimitedTo(bos);
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			cleanUp();
		} finally {
			if(got)
				outStreamSem.release();
		}
	}

	@Override
	public void run() {
		sessionConnected();
		try {
			bis = new BufferedInputStream(socket.getInputStream());
			bos = new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			sessionDisconnected();
			e.printStackTrace();
		}
		try {
			while(!socket.isClosed()) {
				Packet p = Packet.parseDelimitedFrom(bis);
				switch(p.getType()) {
				case OFFER:
					TransferOffer to = p.getTransferOffer();
					if(incomingOfferMap.containsKey(to.getOfferId())) {
						socket.close();
						sessionDisconnected(); 
					} else {
						incomingOfferMap.put(to.getOfferId(), to);
						sessionGotOffer(to);
					}
					break;
				case RESPONSE:
					TransferOffer tor;
					TransferResponse tr = p.getTransferResponse();
					if( (tor = outgoingOfferMap.remove(tr.getOfferId())) == null) {
						//response to non-existing offer
					} else {
						sessionGotResponse(tr.getOfferId(), tr.getAccept());
						String base = basePathsForOutGoingOffers.get(tr.getOfferId());
						FileTransfer ft = new FileTransfer(tor, new File(base), false);
						sessionFileTransferStarted(ft);
						activeFileTransfers.put(tr.getOfferId(), ft);
						ft.start(bos, outStreamSem);

					}
					break;
				case CANCEL:
					TransferCancel tc = p.getCancel();
					TransferOffer rto = incomingOfferMap.remove(tc.getOfferId());
					FileTransfer ft = activeFileTransfers.get(tc.getOfferId());
					ft.cancel();
					sessionGotOfferCancel(tc.getOfferId());
					break;
				case CHAT:
					Chat c = p.getChat();
					sessionGotChat(c.getMessage());
					break;
				case DATA:
					FileData fd = p.getData();
					FileTransfer tf = activeFileTransfers.get(fd.getOfferId());
					if(tf == null) {
						//we have not accepted this offer.
						socket.close();
						sessionDisconnected();
					} else {
						int numBytes =  fd.getNumBytes();
						if( numBytes != tf.consume(bis, numBytes)) {
							//XXX should not be runtimeexception. need to catch in Session.
							throw new IOException("Unable to read/write all data that was supposed to be sent.");
						}
					}
					
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			cleanUp();
		}
		
	}
	public void sendChat(String message) {
		Packet.Builder bb = Packet.newBuilder();
		bb.getChatBuilder().setMessage(message);
		bb.setType(Type.CHAT);
		Packet p = bb.build();
		try {
			p.writeDelimitedTo(bos);
			bos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cleanUp();
		}
	}
	@Override
	public void addListener(SessionListener arg0) {
		audience.addToAudience(arg0, SessionListener.class);
	}
	@Override
	public void sessionConnected() {
		for(Listener l : audience.getAudience(SessionListener.class)) {
			SessionListener sl = (SessionListener) l;
			sl.sessionConnected();
		}
		
	}
	@Override
	public void sessionGotOffer(TransferOffer to) {
		for(Listener l : audience.getAudience(SessionListener.class)) {
			SessionListener sl = (SessionListener) l;
			sl.sessionGotOffer(to);
		}
		
	}
	@Override
	public void sessionGotResponse(long offerId, boolean accept) {
		for(Listener l : audience.getAudience(SessionListener.class)) {
			SessionListener sl = (SessionListener) l;
			sl.sessionGotResponse(offerId, accept);
		}
	}
	@Override
	public void sessionGotChat(String message) {
		for(Listener l : audience.getAudience(SessionListener.class)) {
			SessionListener sl = (SessionListener) l;
			sl.sessionGotChat(message);
		}
	}
	@Override
	public void sessionDisconnected() {
		for(Listener l : audience.getAudience(SessionListener.class)) {
			SessionListener sl = (SessionListener) l;
			sl.sessionDisconnected();
		}
	}
	@Override
	public void sessionFileTransferStarted(FileTransfer ft) {
		for(Listener l : audience.getAudience(SessionListener.class)) {
			SessionListener sl = (SessionListener) l;
			sl.sessionFileTransferStarted(ft);
		}
		
	}
	@Override
	public void sessionFileTransferStopped(FileTransfer ft) {
		for(Listener l : audience.getAudience(SessionListener.class)) {
			SessionListener sl = (SessionListener) l;
			sl.sessionFileTransferStopped(ft);
		}
	}
	@Override
	public void sessionGotOfferCancel(long offerId) {
		for(Listener l : audience.getAudience(SessionListener.class)) {
			SessionListener sl = (SessionListener) l;
			sl.sessionGotOfferCancel(offerId);
		}
	}
	
}
