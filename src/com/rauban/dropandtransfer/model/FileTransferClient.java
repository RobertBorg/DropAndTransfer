package com.rauban.dropandtransfer.model;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.rauban.dropandtransfer.model.io.policy.ResourceInputStream;
import com.rauban.dropandtransfer.model.speaker.baseImpl.FileTransferClientSpeakerBaseImpl;

public class FileTransferClient extends FileTransferClientSpeakerBaseImpl{
	private String[] pathToResources;
	private String addressAndPort;
	
	private Socket socket = null;
	
	private DataStreamer ds;
	private Thread dsThread;
	protected FileTransferClient(String addressAndPort, String... pathToResources) {
		this.pathToResources = pathToResources;
		this.addressAndPort = addressAndPort;
	}
	public void start(){
		String[] splitaddr = addressAndPort.split(":");
		
		try {
			socket = new Socket(splitaddr[0], Integer.parseInt(splitaddr[1]));
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
		try {
			ds = new DataStreamer(new ResourceInputStream(pathToResources), socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dsThread = new Thread(ds);
		dsThread.start();
	}


}
