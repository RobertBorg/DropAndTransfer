package com.rauban.dropandtransfer.model;

import java.io.File;
import java.net.Socket;
import java.util.List;

import com.rauban.dropandtransfer.model.io.DataStreamer;
import com.rauban.dropandtransfer.view.listener.FileTransferClientListener;
import com.rauban.speaker_listener_pattern.speaker.AudienceHolder;
import com.rauban.speaker_listener_pattern.speaker.Speaker;

public abstract class ResourceTransferBase implements Speaker<FileTransferClientListener>, FileTransferClientListener{
	AudienceHolder audience;
	
	// used by subclasses
	protected Socket socket = null;
	protected DataStreamer ds;
	
	private Thread dsThread;
	protected ResourceTransferBase() {
		audience = new AudienceHolder();
	}
	public void start(){
		setUp();
		dsThread = new Thread(ds);
		dsThread.start();
	}
	protected abstract void setUp();
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
	@Override
	public void fatalUnableToParseAddress(String addressAndPort) {
		List<FileTransferClientListener> ftcll = audience.getAudience(FileTransferClientListener.class);
		for(FileTransferClientListener ftcl: ftcll){
			ftcl.fatalUnableToParseAddress(addressAndPort);
		}		
		
	}
	@Override
	public void fatalUnableToOpenSocket() {
		List<FileTransferClientListener> ftcll = audience.getAudience(FileTransferClientListener.class);
		for(FileTransferClientListener ftcl: ftcll){
			ftcl.fatalUnableToOpenSocket();
		}		
	}
	@Override
	public void fatalUnableToGetOutputStreamOfSocket() {
		List<FileTransferClientListener> ftcll = audience.getAudience(FileTransferClientListener.class);
		for(FileTransferClientListener ftcl: ftcll){
			ftcl.fatalUnableToGetOutputStreamOfSocket();
		}		
	}
	abstract public void stop();
	abstract public void abort();
}
