package com.rauban.dropandtransfer.spike;


import com.rauban.dropandtransfer.model.FileTransferClient;


public class FileTransferTestClient {
	public static void main(String[] args) {
			FileTransferClient ftc = new FileTransferClient("./lol.txt", "127.0.0.1", "1337");
			
			ftc.start();
	}

}
