package com.rauban.dropandtransfer.view;

import com.rauban.dropandtransfer.controller.NetworkController;
import com.rauban.dropandtransfer.controller.ResourceTransferClientController;
import com.rauban.dropandtransfer.view.listener.NetworkListener;
import org.fourthline.cling.model.meta.RemoteDevice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame implements NetworkListener
{
    private NetworkController controller;

    private boolean uiInitialized = false;

    private JPanel panel;
    private JList<String> hosts;

    public MainWindow(NetworkController controller)
    {
        this.controller = controller;
        controller.addListener(this);

        setTitle("FileDropper");
        setSize(1024, 768);
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
        panel.setLayout(new GridBagLayout());

        JButton selectFileButton = new JButton("Select File");
        selectFileButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();
                File f = new File("Desktop");
                chooser.setCurrentDirectory(f);
                chooser.setMultiSelectionEnabled(true);
                int returnVal = chooser.showOpenDialog(null);

                List<String> selectedPaths = new ArrayList<String>();
                if (returnVal == JFileChooser.APPROVE_OPTION && hosts.getSelectedIndex() >= 0)
                {
                    for (File file : chooser.getSelectedFiles())
                    {
                        selectedPaths.add(file.getAbsolutePath());
                    }
                    RemoteDevice device = controller.getRemotes().get(hosts.getSelectedIndex());
                    if (device != null)
                    {
                        System.out.println("Selected device: " + device.getIdentity().getDescriptorURL().toString() + " " + device.getDisplayString());
                        //Somehow get the correct URL
                        ResourceTransferClientController transferClientController = controller.transferResource("", selectedPaths.toArray(new String[selectedPaths.size()]));
                        transferClientController.start();
                    }
                }
            }
        });
        panel.add(selectFileButton);

        hosts = new JList<String>();
        hosts.setLayoutOrientation(JList.VERTICAL);
        hosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroller = new JScrollPane(hosts);
        panel.add(listScroller);
        this.updateRemotes();
        controller.search();
        this.uiInitialized = true;
    }

    private void updateRemotes()
    {
        List<RemoteDevice> remoteDevices = controller.getRemotes();
        String[] labels = new String[remoteDevices.size()];
        for (int i = 0; i < remoteDevices.size(); i++)
        {
            labels[i] = remoteDevices.get(i).getDisplayString();
        }
        hosts.setListData(labels);
    }

    @Override
    public void discoveryStarted()
    {
        initializeUI();
    }

    @Override
    public void discoveryRemoteDeviceAdded(RemoteDevice rd)
    {
        updateRemotes();
    }

    @Override
    public void discoveryRemoteDeviceRemoved()
    {
        updateRemotes();
    }
}
