package com.rauban.dropandtransfer.controller;

import com.rauban.dropandtransfer.model.FileTransferClient;
import com.rauban.dropandtransfer.model.listener.FileTransferClientListener;

public class FileTransferClientController {
	private FileTransferClient ftc;
	public FileTransferClientController(FileTransferClient ftc){
		this.ftc = ftc;
	}
	public void addFileTransferClientListener(FileTransferClientListener lis){
//		ftc.addListener(lis);
	} 
}
