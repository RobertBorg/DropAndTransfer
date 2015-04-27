package com.rauban.dropandtransfer.view.listener;

import com.rauban.dropandtransfer.controller.SessionController;
import com.rauban.dropandtransfer.model.Session;
import com.rauban.speaker_listener_pattern.listener.Listener;

import org.fourthline.cling.model.meta.RemoteDevice;

public interface SessionServerListener extends Listener
{
	void sessionAdded(SessionController s);
	
	void sessionRemoved(SessionController s);

}
