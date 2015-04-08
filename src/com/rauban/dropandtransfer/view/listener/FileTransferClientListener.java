package com.rauban.dropandtransfer.view.listener;

import com.rauban.speaker_listener_pattern.listener.Listener;

import java.io.File;

public interface FileTransferClientListener extends Listener
{
    void transferStarted(File resource);

    void transferSuccess(File resource);

    void transferFail(File resource);

    void updateReceivedAmount(long numBytes);

    void fatalUnableToParseAddress(String addressAndPort);

    void fatalUnableToOpenSocket();

    void fatalUnableToGetOutputStreamOfSocket();

}
