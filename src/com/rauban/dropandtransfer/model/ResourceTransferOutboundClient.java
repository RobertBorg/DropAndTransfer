package com.rauban.dropandtransfer.model;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.rauban.dropandtransfer.model.io.DataStreamer;
import com.rauban.dropandtransfer.model.io.ResourceInputStream;

public class ResourceTransferOutboundClient extends ResourceTransferAbstractClient {
	private String addressAndPort;
	private String[] pathToResources;
	private ResourceInputStream ris;

	protected ResourceTransferOutboundClient(String addressAndPort,
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
		ris = new ResourceInputStream(pathToResources);
		try {
			ds = new DataStreamer(ris, socket.getOutputStream());
		} catch (IOException e) {
			fatalUnableToGetOutputStreamOfSocket();
		}
	}

	@Override
	public void stop() {
		ris.stopAfterCurrentFile(true);
	}

	@Override
	public void abort() {
		ris.stopNow();
	}

}
