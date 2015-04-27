package com.rauban.dropandtransfer.controller;

import com.rauban.dropandtransfer.model.Session;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.speaker_listener_pattern.controller.ControllerBaseImpl;

public class SessionController extends ControllerBaseImpl<Session>
{

    public SessionController(Session model)
    {
        super(model);
    }
    
    public void sendTransferOffer(TransferOffer.Builder tob) {
    	model.sendTransferOffer(tob);
    }
    
    public void sendChat(String message) {
    	model.sendChat(message);
    }

}
