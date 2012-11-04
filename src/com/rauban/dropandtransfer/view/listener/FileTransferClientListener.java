package com.rauban.dropandtransfer.view.listener;

import java.io.File;

import com.rauban.speaker_listener_pattern.listener.Listener;


public interface FileTransferClientListener extends Listener {
	public void transferStarted(File resource);
	public void transferSuccess(File resource);
	public void transferFail(File resource);
	
	public void updateReceivedAmount(long numBytes);
}
