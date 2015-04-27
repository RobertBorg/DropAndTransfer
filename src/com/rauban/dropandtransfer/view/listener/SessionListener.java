package com.rauban.dropandtransfer.view.listener;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.speaker_listener_pattern.listener.Listener;
public interface SessionListener extends Listener {
	
	void sessionConnected();
	
	void sessionGotOffer(TransferOffer to);
	
	void sessionGotResponse(long offerId, boolean accept);
	
	void sessionGotOfferCancel(long offerId);
	
	void sessionGotChat(String message);
	
	void sessionFileTransferStarted(FileTransfer ft);
	
	void sessionFileTransferStopped(FileTransfer ft);
	
	void sessionDisconnected();
	
}
