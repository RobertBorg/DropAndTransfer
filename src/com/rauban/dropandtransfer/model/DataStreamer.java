package com.rauban.dropandtransfer.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DataStreamer implements Runnable{
	private InputStream is;
	private OutputStream os;
	private int bufferSize;
	protected DataStreamer(InputStream is, OutputStream os){
		this(is,os,1024);
	}
	protected DataStreamer(InputStream is, OutputStream os,int bufferSize){
		this.is = is;
		this.os = os;
		this.bufferSize = bufferSize;
	}
	@Override
	public void run() {
		byte[] buf = new byte[bufferSize];
		int readNow = 0;
		try {
			while((readNow = is.read(buf)) != -1){
					try{
					os.write(buf, 0, readNow);
					} catch(IOException e){
						//XXX handle output IO exception
						e.printStackTrace();
					}
			}
		} catch (IOException e) {
			//XXX handle input IO Exception
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.flush();
				os.close();
			} catch (IOException e) {
				//XXX handle separately and push different events to gui
				e.printStackTrace();
			}
		}
	}

}
