package com.rauban.dropandtransfer.model;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import com.rauban.dropandtransfer.model.io.policy.ResourceInputStream;
import com.rauban.dropandtransfer.view.listener.FileTransferClientListener;
import com.rauban.speaker_listener_pattern.speaker.AudienceHolder;
import com.rauban.speaker_listener_pattern.speaker.Speaker;

public class FileTransferClient implements Speaker<FileTransferClientListener>, FileTransferClientListener{
	AudienceHolder audience;
	private String[] pathToResources;
	private String addressAndPort;
	
	private Socket socket = null;
	
	private DataStreamer ds;
	private Thread dsThread;
	protected FileTransferClient(String addressAndPort, String... pathToResources) {
		audience = new AudienceHolder();
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
	@Override
	public void addListener(FileTransferClientListener arg0) {
		audience.addToAudience(arg0, FileTransferClientListener.class);
	}
	@Override
	public void transferStarted(File resource) {
		List<FileTransferClientListener> ftcll = audience.getAudience(FileTransferClientListener.class);
		for(FileTransferClientListener ftcl: ftcll){
			ftcl.transferStarted(resource);
		}
	}
	@Override
	public void transferSuccess(File resource) {
		List<FileTransferClientListener> ftcll = audience.getAudience(FileTransferClientListener.class);
		for(FileTransferClientListener ftcl: ftcll){
			ftcl.transferSuccess(resource);
		}		
	}
	@Override
	public void transferFail(File resource) {
		List<FileTransferClientListener> ftcll = audience.getAudience(FileTransferClientListener.class);
		for(FileTransferClientListener ftcl: ftcll){
			ftcl.transferFail(resource);
		}		
	}
	@Override
	public void updateReceivedAmount(long numBytes) {
		List<FileTransferClientListener> ftcll = audience.getAudience(FileTransferClientListener.class);
		for(FileTransferClientListener ftcl: ftcll){
			ftcl.updateReceivedAmount(numBytes);
		}		
	}
}
