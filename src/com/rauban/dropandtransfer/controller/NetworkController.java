package com.rauban.dropandtransfer.controller;

import com.rauban.dropandtransfer.model.Network;
import com.rauban.speaker_listener_pattern.controller.ControllerBaseImpl;

import org.fourthline.cling.model.meta.RemoteDevice;

import java.util.ArrayList;
import java.util.List;

public class NetworkController extends ControllerBaseImpl<Network>
{

    public NetworkController(Network model)
    {
        super(model);
    }

    public void start()
    {
        model.start();
    }

    public void stop()
    {
        model.stop();
    }

    public void search()
    {
        model.search();
    }

    public List<RemoteDevice> getRemotes()
    {
        return model.getRemotes();
    }

}
