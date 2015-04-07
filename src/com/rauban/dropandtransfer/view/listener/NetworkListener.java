package com.rauban.dropandtransfer.view.listener;

import com.rauban.speaker_listener_pattern.listener.Listener;
import org.fourthline.cling.model.meta.RemoteDevice;

public interface NetworkListener extends Listener
{
    void discoveryStarted();

    void discoveryRemoteDeviceAdded(RemoteDevice rd);

    void discoveryRemoteDeviceRemoved();

}
