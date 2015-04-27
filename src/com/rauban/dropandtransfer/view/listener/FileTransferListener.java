package com.rauban.dropandtransfer.view.listener;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.speaker_listener_pattern.listener.Listener;
public interface FileTransferListener extends Listener {
	
	void sessionFileTransferStatusUpdate(long offerId, String currentFile, float currentSpeedInKBytesPerSecond, float currentAvgSpeedInKBytesPerSecond, float currentFilePercent, float currentTransferPercent);
	
}
