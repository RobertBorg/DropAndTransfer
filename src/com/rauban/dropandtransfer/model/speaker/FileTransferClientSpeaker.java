package com.rauban.dropandtransfer.model.speaker;

import java.io.File;


public interface FileTransferClientSpeaker extends Speaker {
	public void transferStarted(File resource);
	public void transferComplete(File resource);
}
