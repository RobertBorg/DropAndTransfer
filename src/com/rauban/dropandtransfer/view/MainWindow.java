package com.rauban.dropandtransfer.view;

import com.rauban.dropandtransfer.controller.NetworkController;
import com.rauban.dropandtransfer.view.listener.NetworkListener;
import org.fourthline.cling.model.meta.RemoteDevice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements NetworkListener
{
    private NetworkController controller;

    private boolean uiInitialized = false;

    private JPanel panel;

    public MainWindow(NetworkController controller)
    {
        this.controller = controller;
        controller.addListener(this);

        setTitle("FileDropper");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel = new JPanel();
        JLabel loadingLabel = new JLabel("Loading...");
        panel.setLayout(new GridBagLayout());
        panel.add(loadingLabel);
        this.add(panel);

        //It's probably OK to always start this
        controller.start();
    }

    private synchronized void initializeUI()
    {
        if (uiInitialized)
            return;
        panel.removeAll();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridLayout(5, 5, 5, 5));
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.search();
            }
        });
        panel.add(searchButton);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.stop();
            }
        });
        panel.add(stopButton);

        this.uiInitialized = true;
    }

    @Override
    public void discoveryStarted()
    {
        initializeUI();
    }

    @Override
    public void discoveryRemoteDeviceAdded(RemoteDevice rd)
    {
        System.out.println("Discovered " + rd.getDisplayString());
    }
}
