package com.rauban.dropandtransfer.spike;


import com.rauban.dropandtransfer.model.FileTransferServer;


public class FileTransferTestServer {
	public static void main(String[] args) {
			FileTransferServer fts = new FileTransferServer("./Received/");
			
			fts.run();
	}

}
