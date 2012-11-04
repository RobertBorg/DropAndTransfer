package com.rauban.dropandtransfer.view.listener;

import org.fourthline.cling.model.meta.RemoteDevice;

import com.rauban.speaker_listener_pattern.listener.Listener;


public interface NetworkListener extends Listener {
	public void discoveryStarted();
	public void discoveryRemoteDeviceAdded(RemoteDevice rd);
}
