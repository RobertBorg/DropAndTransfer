package com.rauban.dropandtransfer.model.speaker;

import java.io.File;

import com.rauban.speaker_listener_pattern.speaker.Speaker;


public interface FileTransferClientSpeaker extends Speaker {
	public void transferStarted(File resource);
	public void transferComplete(File resource);
}
