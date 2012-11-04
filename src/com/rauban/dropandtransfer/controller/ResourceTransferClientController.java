package com.rauban.dropandtransfer.controller;

import com.rauban.dropandtransfer.model.ResourceTransferBase;
import com.rauban.speaker_listener_pattern.controller.ControllerBaseImpl;

public class ResourceTransferClientController extends ControllerBaseImpl<ResourceTransferBase> {

	public ResourceTransferClientController(ResourceTransferBase model) {
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
