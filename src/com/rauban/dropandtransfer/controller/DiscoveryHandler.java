package com.rauban.dropandtransfer.controller;


import java.util.Observer;

import com.rauban.dropandtransfer.model.Network;

public class DiscoveryHandler {
	private Network model;
	
	public DiscoveryHandler(Network model){
		this.model = model;
	}
	public void start() {
		model.start();
	}
	public void stop() {
		model.stop();
	}
	
	public void search() {
		model.search();
	}
	
	public FileTransferClientController startTransfer(Observer obs, String connectionAddress, String...pathToSend){
		model.startNewClient(obs,connectionAddress, pathToSend);
		//XXX implement
		return null;
	}
}
