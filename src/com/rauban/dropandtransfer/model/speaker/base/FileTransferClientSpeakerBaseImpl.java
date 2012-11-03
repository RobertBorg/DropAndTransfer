package com.rauban.dropandtransfer.model.speaker.base;

import java.io.File;

import com.rauban.dropandtransfer.model.listener.FileTransferClientListener;
import com.rauban.dropandtransfer.model.speaker.FileTransferClientSpeaker;
import com.rauban.speaker_listener_pattern.speaker.SpeakerBaseImpl;

public class FileTransferClientSpeakerBaseImpl extends SpeakerBaseImpl<FileTransferClientListener> implements FileTransferClientSpeaker  {

	@Override
	public void transferStarted(File resource) {
		for(FileTransferClientListener l: listeners){
			l.transferStarted(resource);
		}
	}

	@Override
	public void transferComplete(File resource) {
		for(FileTransferClientListener l: listeners){
			l.transferComplete(resource);
		}		
	}
	

}
