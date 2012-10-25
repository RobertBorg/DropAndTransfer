package com.rauban.dropandtransfer.controller;


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
}
