package com.rauban.dropandtransfer.controller;



import com.rauban.dropandtransfer.model.Network;
import com.rauban.speaker_listener_pattern.controller.ControllerBaseImpl;

public class NetworkController extends ControllerBaseImpl<Network> {
	
	public NetworkController(Network model){
		super(model);
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
	
	public FileTransferClientController transferResource(String connectionAddress, String...pathToSend){
		return new FileTransferClientController(model.transferResource(connectionAddress, pathToSend));
	}
}
