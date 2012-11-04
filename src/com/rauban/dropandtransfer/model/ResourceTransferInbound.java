package com.rauban.dropandtransfer.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.rauban.dropandtransfer.model.io.DataStreamer;
import com.rauban.dropandtransfer.model.io.ResourceOutputStream;
import com.rauban.dropandtransfer.model.io.policy.file_exists.OnFileExistsGenerateNewName;

public class ResourceTransferInbound extends ResourceTransferBase {
	private String downloadPath;
	private InputStream is = null;
	private ResourceOutputStream ros = null;
	public ResourceTransferInbound(Socket socket, String downloadPath) {
		super();
		this.socket = socket; 
		this.downloadPath = downloadPath;
	}

	@Override
	protected void setUp() {
		try {
			is = socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ros = new ResourceOutputStream(OnFileExistsGenerateNewName.class,downloadPath);
		ds = new DataStreamer(is, ros);
	}

	@Override
	public void stop() {
		ros.setStopAfterCurrentFile(true);
		
	}

	@Override
	public void abort() {
		ros.stopNow();
	}

}
