package com.rauban.dropandtransfer.view.listener;

import com.rauban.speaker_listener_pattern.listener.Listener;

public interface ResourceInputStreamListener extends Listener {
	public void onNewOutboundResource(String resource, long size, boolean isDirectory);
	public void onResourceTransferEnded();
}
