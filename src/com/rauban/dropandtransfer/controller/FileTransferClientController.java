package com.rauban.dropandtransfer.controller;

import com.rauban.dropandtransfer.model.FileTransferClient;
import com.rauban.dropandtransfer.view.listener.FileTransferClientListener;
import com.rauban.speaker_listener_pattern.controller.ControllerBaseImpl;

public class FileTransferClientController extends ControllerBaseImpl<FileTransferClient,FileTransferClientListener> {

	public FileTransferClientController(FileTransferClient model) {
		super(model);
	}
}
