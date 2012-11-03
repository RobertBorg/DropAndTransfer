package com.rauban.dropandtransfer.model.speaker;

import org.fourthline.cling.model.meta.RemoteDevice;

import com.rauban.speaker_listener_pattern.speaker.Speaker;

public interface NetworkSpeaker extends Speaker {
	public void discoveryStarted();
	public void discoveryRemoteDeviceAdded(RemoteDevice rd);
}
