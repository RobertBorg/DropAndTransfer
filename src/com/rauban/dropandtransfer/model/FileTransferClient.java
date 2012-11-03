package com.rauban.dropandtransfer.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.rauban.dropandtransfer.model.protocol.FileTransfer.FileDropHeader;
import com.rauban.dropandtransfer.model.speaker.baseImpl.FileTransferClientSpeakerBaseImpl;

public class FileTransferClient extends FileTransferClientSpeakerBaseImpl implements Runnable{
	private static final int BUFFER_SIZE = 4096;
	private String[] pathToResources;
	private String remoteIp;
	private String remotePort;
	protected FileTransferClient(String addressAndPort, String... pathToResources) {
		this.pathToResources = pathToResources;
		String[] ipAndPort = addressAndPort.split(":");
		if(ipAndPort.length == 2){
			this.remoteIp = ipAndPort[0];
			this.remotePort = ipAndPort[1];
		} else {
			//XXX state= bad Address
		}
	}
	public void run(){
		Socket s = null;
		try {
			s = new Socket(remoteIp,Integer.parseInt(remotePort));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String pathToResource : pathToResources ){
			transferResource(s, pathToResource);
		}


	}
	private void transferResource(Socket socket, String pathToResource) {
		File f = new File(pathToResource);
		transferStarted(f);
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(f));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		long size = f.length();

		FileDropHeader fdh = FileDropHeader.newBuilder().setResourceName(f.getName()).setIsDir(f.isDirectory()).setSize(size).build();
		OutputStream os = null;
		try {
			os = socket.getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] headerBuf= fdh.toByteArray();

		try {
			os.write(headerBuf.length);
			os.write(headerBuf);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long read = 0;
		long readNow = 0;
		byte[] buf = new byte[BUFFER_SIZE];
		while (read < size && readNow != -1){
			try {
				long toRead = size - read;
				if( toRead > buf.length){
					readNow = bis.read(buf);
				} else {
					readNow = bis.read(buf,0,(int) toRead);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			try {
				os.write(buf, 0, (int) readNow);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				readNow = -1;
				continue;
			}

			if(readNow != -1){
				read += readNow;
			}

		}
		try {
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(read == size){
				transferSuccess(f);
			} else {
				transferFail(f); //XXX might be incorrect if flush fails.
			}
		}
	}
}
