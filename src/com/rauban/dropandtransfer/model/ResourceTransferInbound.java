package com.rauban.dropandtransfer.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.rauban.dropandtransfer.model.io.DataStreamer;
import com.rauban.dropandtransfer.model.io.ResourceOutputStream;
import com.rauban.dropandtransfer.model.io.policy.file_exists.OnFileExistsGenerateNewName;

public class ResourceTransferInbound extends ResourceTransferBase {
	private String downloadPath;
	public ResourceTransferInbound(Socket socket, String downloadPath) {
		super();
		this.socket = socket; 
		this.downloadPath = downloadPath;
	}

	@Override
	protected void setUp() {
		InputStream is = null;
		try {
			is = socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ds = new DataStreamer(is, new ResourceOutputStream(OnFileExistsGenerateNewName.class,downloadPath));
	}

}
