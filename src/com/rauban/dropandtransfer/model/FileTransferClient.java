package com.rauban.dropandtransfer.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader;

public class FileTransferClient extends Thread {
	private static final int BUFFER_SIZE = 4096;
	private String pathToResource;
	private String remoteIp;
	private String remotePort;
	public FileTransferClient(String pathToResource, String ip, String port) {
		this.pathToResource = pathToResource;
		this.remoteIp = ip;
		this.remotePort = port;
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
		File f = new File(pathToResource);
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
			os = s.getOutputStream();
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
				readNow = bis.read(buf);
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
		}
		
		
	}
}
