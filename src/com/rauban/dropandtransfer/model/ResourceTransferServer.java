package com.rauban.dropandtransfer.model;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import com.rauban.dropandtransfer.controller.ResourceTransferClientController;
import com.rauban.dropandtransfer.view.listener.ResourceTransferServerListener;
import com.rauban.speaker_listener_pattern.speaker.AudienceHolder;
import com.rauban.speaker_listener_pattern.speaker.Speaker;


public class ResourceTransferServer implements Runnable, Speaker<ResourceTransferServerListener>, ResourceTransferServerListener{
	private ServerSocket ss;
	private boolean isRunning;
	private String baseDownloadDir;
	private int port;
	
	private AudienceHolder audience;
	protected ResourceTransferServer(String baseDownloadDir){
		this.baseDownloadDir = baseDownloadDir;
		audience = new AudienceHolder();
	}
	@Override
	public void run() {
		isRunning = true;
		try {
			ss = new ServerSocket(0);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		port = ss.getLocalPort();
		Socket s = null;
		while(isRunning) {
			try {
				s = ss.accept();
			}catch(SocketException se){
				//silently ignore
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if( s != null){
				handleConnection(s);
			}
		}
	}
	private void handleConnection(Socket s) {
		ResourceTransferInboundClient rti = new ResourceTransferInboundClient(s, baseDownloadDir);
		
		
	}
	public void die() {
		isRunning = false;
		while(!ss.isClosed()){
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public int getPort() {
		return port;
	}
	public String getLocalAddress() {
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void addListener(ResourceTransferServerListener arg0) {
		audience.addToAudience(arg0, ResourceTransferServerListener.class);
		
	}
	@Override
	public void onNewInboundTransfer(ResourceTransferClientController rtcc) {
		List<ResourceTransferServerListener> rtsll = audience.getAudience(ResourceTransferServerListener.class);
		for(ResourceTransferServerListener rtsl : rtsll){
			rtsl.onNewInboundTransfer(rtcc);
		}
	}

}
