package com.rauban.dropandtransfer.model.speaker;

import java.io.File;

import com.rauban.dropandtransfer.model.SpeakerBaseImpl;
import com.rauban.dropandtransfer.model.listener.FileTransferClientListener;

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
