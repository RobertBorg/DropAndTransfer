package com.rauban.dropandtransfer.controller;

import com.rauban.dropandtransfer.model.ResourceTransferBase;
import com.rauban.speaker_listener_pattern.controller.ControllerBaseImpl;

public class FileTransferClientController extends ControllerBaseImpl<ResourceTransferBase> {

	public FileTransferClientController(ResourceTransferBase model) {
		super(model);
	}
}
