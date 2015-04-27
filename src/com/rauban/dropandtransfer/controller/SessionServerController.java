package com.rauban.dropandtransfer.controller;

import java.net.URI;

import com.rauban.dropandtransfer.model.Network;
import com.rauban.dropandtransfer.model.SessionServer;
import com.rauban.speaker_listener_pattern.controller.ControllerBaseImpl;

public class SessionServerController extends ControllerBaseImpl<SessionServer>{
	public SessionServerController(SessionServer model) {
		super(model);
		// TODO Auto-generated constructor stub
	}

	public boolean startSession(URI uri) {
		return model.startSession(uri);
	}
}
