package com.rauban.dropandtransfer.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.ibm.icu.text.UnicodeSet;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.Chat;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.FileData;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.Packet;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.Packet.Builder;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.Packet.Type;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferCancel;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferResponse;
import com.rauban.dropandtransfer.util.FileUtil;
import com.rauban.dropandtransfer.view.listener.SessionListener;
import com.rauban.speaker_listener_pattern.listener.Listener;
import com.rauban.speaker_listener_pattern.speaker.AudienceHolder;
import com.rauban.speaker_listener_pattern.speaker.Speaker;

public class Session implements Runnable, Speaker<SessionListener>, SessionListener {
	private Socket socket;
	private AudienceHolder audience;
	private CodedOutputStream cos;
	private CodedInputStream cis;
	
	private BufferedOutputStream bos;
	private BufferedInputStream bis;
	
	long nextOutGoingTransferOffferId; 
	private HashMap<Long, TransferOffer> incommingOfferMap;
	private HashMap<Long, TransferOffer> outgoingOfferMap;
	
	private HashMap<Long, FileTransfer> activeFileTransfers;
	
	public Session(Socket socket) {
		init(socket);
	}
	public Session(InetAddress address, int port) throws IOException {
		init(new Socket(address, port));
	}
	private void init(Socket s) {
		this.socket = s;
		audience = new AudienceHolder();
		incommingOfferMap = new HashMap<Long, TransferOffer>();
		outgoingOfferMap = new HashMap<Long, TransferOffer>();
		activeFileTransfers = new HashMap<Long, FileTransfer>();
	}
	private void cleanUp() {
		try {
			cos.flush();
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
		TransferOffer to = incommingOfferMap.remove(tr.getOfferId());

		Packet.Builder p = Packet.newBuilder();
		p.setType(Type.RESPONSE);
		p.setTransferResponse(tr);
		sendPacket(p.build());
		
		if(tr.getAccept()) {
			FileTransfer ft = new FileTransfer(to, new File("./Received/"), true);
			activeFileTransfers.put(to.getOfferId(), ft);
			sessionFileTransferStarted(ft);
			incommingOfferMap.remove(to.getOfferId());
		}
		return true;
	}

	public TransferOffer createTransferOffer(File[] resources) {
		if(resources == null || resources.length == 0)
			return null;

		TransferOffer.Builder tob = TransferOffer.newBuilder();
		tob.setOfferId(nextOutGoingTransferOffferId++);
		String base = resources[0].getParentFile().getAbsolutePath();
		FileUtil.iterateResources(tob, resources, base);
		TransferOffer to = tob.build();
		return to;
	}

	public void sendTransferOffer(TransferOffer to) {
		Packet.Builder pb = Packet.newBuilder();
		pb.setType(Type.OFFER);
		pb.setTransferOffer(to);
		outgoingOfferMap.put(to.getOfferId(), to);
		sendPacket(pb.build());
	}

	private void sendPacket(Packet p) {
		try {
			p.writeDelimitedTo(bos);
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			cleanUp();
		}
	}

	@Override
	public void run() {
		sessionConnected();
		try {
			bis = new BufferedInputStream(socket.getInputStream());
			cis = CodedInputStream.newInstance(bis);

			bos = new BufferedOutputStream(socket.getOutputStream());
			cos = CodedOutputStream.newInstance(bos);
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
					if(incommingOfferMap.containsKey(to.getOfferId())) {
						socket.close();
						sessionDisconnected(); 
					} else {
						incommingOfferMap.put(to.getOfferId(), to);
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
						FileTransfer ft = new FileTransfer(tor, new File("./Received/"), false);
						activeFileTransfers.put(tr.getOfferId(), ft);
						ft.start(bos);
					}
					break;
				case CANCEL:
					TransferCancel tc = p.getCancel();
					TransferOffer rto = incommingOfferMap.remove(tc.getOfferId());
					sessionGotOfferCancel(rto.getOfferId());
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
