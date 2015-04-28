package com.rauban.dropandtransfer.controller;

import com.rauban.dropandtransfer.model.Session;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.speaker_listener_pattern.controller.ControllerBaseImpl;

import java.io.File;

public class SessionController extends ControllerBaseImpl<Session>
{

    public SessionController(Session model)
    {
        super(model);
    }
    
    public void sendTransferOffer(File[] resources) {
    	model.sendTransferOffer(resources);
    }
    
    public void sendChat(String message) {
    	model.sendChat(message);
    }

}
