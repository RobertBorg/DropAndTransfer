package com.rauban.dropandtransfer.model;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.rauban.dropandtransfer.model.io.DataStreamer;
import com.rauban.dropandtransfer.model.io.ResourceInputStream;

public class ResourceTransferOutbound extends ResourceTransferBase {
	private String addressAndPort;
	private String[] pathToResources;

	protected ResourceTransferOutbound(String addressAndPort,
			String[] pathToResources) {
		super();
		this.addressAndPort = addressAndPort;
		this.pathToResources = pathToResources;
	}
	
	protected void setUp (){
		String[] splitaddr = addressAndPort.split(":");
		
		try {
			socket = new Socket(splitaddr[0], Integer.parseInt(splitaddr[1]));
		} catch (NumberFormatException e) {
			fatalUnableToParseAddress(addressAndPort);
		} catch (UnknownHostException e) {
			fatalUnableToParseAddress(addressAndPort); // XXX maybe we should have like incorrectAddress instead?
		} catch (IOException e) {
			fatalUnableToOpenSocket();
		}
		try {
			ds = new DataStreamer(new ResourceInputStream(pathToResources), socket.getOutputStream());
		} catch (IOException e) {
			fatalUnableToGetOutputStreamOfSocket();
		}
	}

}
