package com.rauban.dropandtransfer.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.protobuf.InvalidProtocolBufferException;
import com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader;

public class FileTransferServer implements Runnable {
	private static final int FILE_WRITE_BUFFER = 4096;
	private ServerSocket ss;
	private boolean isRunning;
	private String baseDownloadDir;
	public FileTransferServer(String baseDownloadDir){
		this.baseDownloadDir = baseDownloadDir;
		try {
			ss = new ServerSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		isRunning = true;
		Socket s = null;
		while(isRunning) {
			try {
				s = ss.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			InputStream is = null;
			try {
				is = s.getInputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			short size = 0;
			try {
				size = (short) (0xFF & is.read());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			byte[] headerBuf = new byte[size];
			int read = 0;
			while(read < size || read != -1){
				try {
					read += is.read(headerBuf);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					read = -1;
				}
			}
			FileDropHeader fdh = null;
			try {
				fdh = FileDropHeader.parseFrom(headerBuf);
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File outFile = new File(baseDownloadDir + fdh.getResourceName());
			OutputStream os = null;
			try {
				os = new FileOutputStream(outFile);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			read = 0;
			byte[] toFileBuf = new byte[FILE_WRITE_BUFFER];
			int readNow = 0; 
			while(read < fdh.getSize() && readNow != -1){
				try {
					readNow = is.read(toFileBuf);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(readNow != -1){
					read += readNow;
					try {
						os.write(toFileBuf, 0, readNow);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			

		}
	}
	public void die() {
		isRunning = false;
	}

}