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

import com.rauban.dropandtransfer.model.protocol.FileTransfer.ResourceHeader;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.dropandtransfer.view.listener.FileTransferListener;
import com.rauban.speaker_listener_pattern.speaker.AudienceHolder;
import com.rauban.speaker_listener_pattern.speaker.Speaker;

public class FileTransfer implements Speaker<FileTransferListener> {
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
	
	private boolean doTerminate; // needs locking; only used for sending
	private  AudienceHolder audience;
	
	public FileTransfer(TransferOffer to, File baseFolder, boolean receive) {
		this.receive = receive;
		currentFileIndex = -1;
		this.to = to;
		this.baseFolder = baseFolder;
		audience = new  AudienceHolder(); 
	}	
	
	public int consume(InputStream is, int numBytes) throws IOException {
		if(!receive) {
			throw new IOException("FileTransfer was not initialized in receiving state.");
		}
		int consumed = 0;
		final int RECV_BUFFER_SIZE = 1024*4;
		byte[] buffer = new byte[RECV_BUFFER_SIZE];
		while(numBytes != consumed) {
			int remaining = numBytes - consumed;
			int read;
			try {
				read = is.read(buffer, 0, remaining < RECV_BUFFER_SIZE ? remaining : RECV_BUFFER_SIZE);
				consumed += read;
				if(!doTerminate)
					bos.write(buffer, 0 , read);
				current += numBytes;
				currentTotal += numBytes;
				if(current == size) {
					startNextFile();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return consumed;
			}
		}
		return consumed;
	}
	
	public void start(OutputStream o) {
		if(st != null) {
			//terminate. attempt to restart started transfer
			throw new RuntimeException("FileTranfer already started."); 
		}
		st = new SendingThread(o);
		st.start();
	}
	
	private void startNextFile() {
		ResourceHeader rh = to.getResources(++currentFileIndex);
		currentFile = new File(baseFolder.getAbsolutePath() + rh.getResourceName());
			
		try {
			if(receive) {
				bos = new BufferedOutputStream(new FileOutputStream(currentFile));
			} else {
				bis = new BufferedInputStream(new FileInputStream(currentFile));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class SendingThread extends Thread {
		private OutputStream o;
		public SendingThread(OutputStream o) {
			this.o = o;
		}
		public void run() {
			final int SEND_BUFFER_SIZE = 4*1024;
			byte[] buffer = new byte[SEND_BUFFER_SIZE];
			try {
				while(!doTerminate) {
						int read = bis.read(buffer);
						bos.write(buffer, 0, read);
						current+=read;
						currentTotal+=read;
						if(current == size)
							startNextFile();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addListener(FileTransferListener arg0) {
		audience.addToAudience(arg0, FileTransferListener.class);
		}

}
