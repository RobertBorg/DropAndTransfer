package com.rauban.dropandtransfer.controller;

import com.rauban.dropandtransfer.model.ResourceTransferAbstractClient;
import com.rauban.speaker_listener_pattern.controller.ControllerBaseImpl;

public class ResourceTransferClientController extends ControllerBaseImpl<ResourceTransferAbstractClient> {

	public ResourceTransferClientController(ResourceTransferAbstractClient model) {
		super(model);
	}
	public void start(){
		model.start();
	}
	/**
	 * graceful stop
	 */
	public void stop(){
		model.stop();
	}
	public void abort(){
		model.abort();
	}
}
