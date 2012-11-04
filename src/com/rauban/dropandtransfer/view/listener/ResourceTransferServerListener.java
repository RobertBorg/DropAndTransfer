package com.rauban.dropandtransfer.view.listener;

import com.rauban.dropandtransfer.controller.ResourceTransferClientController;
import com.rauban.speaker_listener_pattern.listener.Listener;


public interface ResourceTransferServerListener extends Listener {
	public void onNewInboundTransfer(ResourceTransferClientController rtcc);
}
