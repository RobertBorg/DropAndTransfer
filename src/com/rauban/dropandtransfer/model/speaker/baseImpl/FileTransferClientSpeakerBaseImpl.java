package com.rauban.dropandtransfer.model.speaker.baseImpl;

import java.io.File;

import com.rauban.dropandtransfer.model.speaker.FileTransferClientSpeaker;
import com.rauban.dropandtransfer.view.listener.FileTransferClientListener;
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

	@Override
	public void updateReceivedAmount(long numBytes) {
		for(FileTransferClientListener l: listeners){
			l.updateReceivedAmount(numBytes);
		}	
	}
	

}
