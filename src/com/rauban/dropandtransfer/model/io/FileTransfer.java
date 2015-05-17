package com.rauban.dropandtransfer.model.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import com.rauban.dropandtransfer.model.protocol.FileTransfer.FileData;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.Packet;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.ResourceHeader;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.dropandtransfer.view.listener.FileTransferListener;
import com.rauban.speaker_listener_pattern.listener.Listener;
import com.rauban.speaker_listener_pattern.speaker.AudienceHolder;
import com.rauban.speaker_listener_pattern.speaker.Speaker;

public class FileTransfer implements Speaker<FileTransferListener> {
	private long fileTransferStart;
	private long segmentStart;
	private TransferOffer to;
	private File baseFolder;

	private int currentFileIndex;

	private File currentFile;

	private long size;
	private long current;

	private long currentTotal;
	private long sizeTotal;

	private BufferedOutputStream bos;
	private BufferedInputStream bis;

	private boolean receive;
	
	private SendingThread st;

	private Semaphore sem;
	
	private boolean doTerminate; // needs locking; only used for sending
	private  AudienceHolder audience;
	Map <String, Long> pathOfferIdList;
	private long totalSize;

	public long getOfferId () {
		return this.to.getOfferId();
	}
	public FileTransfer(TransferOffer to, File baseFolder, boolean receive) {
		audience = new AudienceHolder();
		this.receive = receive;
		currentFileIndex = -1;
		this.to = to;
		this.baseFolder = baseFolder;
		pathOfferIdList = new HashMap <String, Long>(); 
		pathOfferIdList.put(baseFolder.getPath(), to.getOfferId());
		fileTransferStart = System.currentTimeMillis();
		for(ResourceHeader rh: to.getResourcesList()) {
			sizeTotal += rh.getSize();
		}
	}
	
	public int consume(InputStream is, int numBytes) throws IOException {
		segmentStart = System.currentTimeMillis();
		if(!receive) {
			throw new IOException("FileTransfer was not initialized in receiving state.");
		}
		int consumed = 0;
		final int RECV_BUFFER_SIZE = SendingThread.SEGMENT_SIZE;
		byte[] buffer = new byte[RECV_BUFFER_SIZE];
		if(bos == null)
			startNextFile(null);
		while(numBytes != consumed) {
			int remaining = numBytes - consumed;
			int read;
			try {
				read = is.read(buffer, 0, remaining < RECV_BUFFER_SIZE ? remaining : RECV_BUFFER_SIZE);
				consumed += read;
				/*
				  if we're canceling the transfer we still need to read the data, but can discard it.
				 */
				if(!doTerminate)
					bos.write(buffer, 0 , read);
				current += read;
				currentTotal += read;
				updateListener();
				if(current == size) {
					startNextFile(null);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return consumed;
			}
		}
		return consumed;
	}
	
	public void start(OutputStream o, Semaphore sem) {
		if(st != null) {
			//terminate. attempt to restart started transfer
			throw new RuntimeException("FileTransfer already started.");
		}
		this.sem = sem;
		st = new SendingThread(o);
		try {
			sem.acquire(); //XXX handle gracefully
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		st.start();
	}
	
	private void startNextFile(OutputStream o) {
		if(bis != null) try {
			bis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bos != null) try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (++currentFileIndex == to.getResourcesCount()) {
			doTerminate = true;
			return;
		}

		ResourceHeader rh = to.getResources(currentFileIndex);
		currentFile = new File(baseFolder.getAbsolutePath() + '/' + rh.getResourceName());
		if(rh.getIsDir()){
			if(!currentFile.exists()){
				currentFile.mkdirs();
			}
			startNextFile(o);
			return;
		} else {
			File parent = currentFile.getParentFile();
			if(!parent.exists()) {
				parent.mkdirs();
			}
		}


		size = rh.getSize();
		current = 0;
		try {
			if(receive) {
				
				bos = new BufferedOutputStream(new FileOutputStream(currentFile));
			} else {
				bis = new BufferedInputStream(new FileInputStream(currentFile));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long getSize() {
		return size;
	}

	public void cancel() {
		System.out.println("canceled");
		doTerminate = true;
	}

	public long getTotalSize() {
		return totalSize;
	}

	private class SendingThread extends Thread {
		private OutputStream o;
		public static  final int SEGMENT_SIZE = 1 << 20;
		public SendingThread(OutputStream o) {
			this.o = o;
		}

		private void startNextSegment() {
			sem.release();
			try {
				sem.acquire(); //XXX handle gracefully please
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				Packet.Builder pb = Packet.newBuilder();
				pb.setType(Packet.Type.DATA);
				FileData.Builder fdb =  pb.getDataBuilder();
				fdb.setOfferId(to.getOfferId());
				//currently we send an entire file in one segment; for larger files this will hamper our ability
				//to send chat and cancel an ongoing transfer.
				fdb.setNumBytes((int)(size - current < SEGMENT_SIZE ? size - current : SEGMENT_SIZE));
				//this starts a new segment
				pb.build().writeDelimitedTo(o);
				segmentStart = System.currentTimeMillis();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			final int SEND_BUFFER_SIZE = SEGMENT_SIZE;
			byte[] buffer = new byte[SEND_BUFFER_SIZE];
			try {
				if(bis == null) {
					startNextFile(o);
				}
				while(!doTerminate) {
					if(current == size)
						startNextFile(o);

					startNextSegment();
					long segmentEnd = current + SEGMENT_SIZE >= size ? size : current + SEGMENT_SIZE;
					while (current != segmentEnd) {
						int remaining =(int)(segmentEnd - current);
						int read;
						if( (read = bis.read(buffer, 0, remaining < SEND_BUFFER_SIZE ? remaining : SEND_BUFFER_SIZE)) == -1) {
							startNextFile(o);
							continue;
						}
						o.write(buffer, 0, read);
						current+=read;
						currentTotal+=read;
						updateListener();
					}
				}
				//needed to push out the last parts of the current file
				o.flush();
				sem.release();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void addListener(FileTransferListener arg0) {
		audience.addToAudience(arg0, FileTransferListener.class);
	}

	/**
	 * updates the status on listeners
	 */
	public synchronized void updateListener() {
		long currentTime = System.currentTimeMillis();
		for (Listener l : audience.getAudience(FileTransferListener.class)) {
			Listener ul = (FileTransferListener) l;
			float currentSpeed = (1000*current/(float)(currentTime - segmentStart))/(float)1024;
			float currentAvgSpeed = (1000*currentTotal/(float)(currentTime - fileTransferStart))/(float)1024;
			float currentFilePercent = 100*current/(float)size;
			float currentTransferPercent = 100*currentTotal/(float)sizeTotal;
			((FileTransferListener) ul).sessionFileTransferStatusUpdate(
					to.getOfferId(), to.getResourcesList()
							.get(currentFileIndex).getResourceName(), currentSpeed,	currentAvgSpeed,
					currentFilePercent, currentTransferPercent);
		}
	}


}
