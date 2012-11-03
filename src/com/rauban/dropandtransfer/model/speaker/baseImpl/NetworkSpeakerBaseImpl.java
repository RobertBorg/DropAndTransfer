package com.rauban.dropandtransfer.model.speaker.baseImpl;

import org.fourthline.cling.model.meta.RemoteDevice;

import com.rauban.dropandtransfer.model.speaker.NetworkSpeaker;
import com.rauban.dropandtransfer.view.listener.NetworkListener;
import com.rauban.speaker_listener_pattern.model.ModelBaseImpl;

public class NetworkSpeakerBaseImpl extends ModelBaseImpl<NetworkListener> implements NetworkSpeaker  {

	@Override
	public void discoveryStarted() {
		for(NetworkListener l : listeners){
			l.discoveryStarted();
		}
	}

	@Override
	public void discoveryRemoteDeviceAdded(RemoteDevice rd) {
		for(NetworkListener l : listeners){
			l.discoveryRemoteDeviceAdded(rd);
		}
		
	}

}
