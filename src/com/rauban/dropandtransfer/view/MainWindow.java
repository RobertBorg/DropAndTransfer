package com.rauban.dropandtransfer.view;

import com.rauban.dropandtransfer.controller.NetworkController;
import com.rauban.dropandtransfer.controller.ResourceTransferClientController;
import com.rauban.dropandtransfer.view.listener.FileTransferClientListener;
import com.rauban.dropandtransfer.view.listener.NetworkListener;
import com.rauban.dropandtransfer.view.listener.ResourceTransferServerListener;
import org.fourthline.cling.model.meta.RemoteDevice;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame implements NetworkListener, ResourceTransferServerListener, FileTransferClientListener
{
    private NetworkController controller;

    private boolean uiInitialized = false;

    private JPanel panel;
    JTextArea logWindow;
    private JList<String> hosts;

    private List<RemoteDevice> localDevices;

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

        logWindow = new JTextArea();
        panel.add(logWindow);
        JButton selectFileButton = new JButton("Select File");
        final MainWindow view = this;
        selectFileButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                RemoteDevice device = localDevices.get(hosts.getSelectedIndex());
                System.out.println("Picked device: " + device.getDisplayString());
                JFileChooser chooser = new JFileChooser();
                File f = new File("Desktop");
                chooser.setCurrentDirectory(f);
                chooser.setMultiSelectionEnabled(true);
                int returnVal = chooser.showOpenDialog(null);

                List<String> selectedPaths = new ArrayList<String>();
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    for (File file : chooser.getSelectedFiles())
                    {
                        selectedPaths.add(file.getAbsolutePath());
                    }
                    ResourceTransferClientController transferClientController = controller
                            .transferResource(device.getDetails().getModelDetails().getModelNumber(), selectedPaths.toArray(new String[selectedPaths.size()]));
                    transferClientController.addListener(view);
                    transferClientController.start();
                }
            }
        });
        panel.add(selectFileButton);

        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, "Ze buttons, zey do nothing!");
            }
        });
        panel.add(sendMessageButton);

        hosts = new JList<String>();
        hosts.setLayoutOrientation(JList.VERTICAL);
        hosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroller = new JScrollPane(hosts);
        panel.add(listScroller);
        this.updateRemotes();
        controller.search();
        this.uiInitialized = true;
    }

    private synchronized void updateRemotes()
    {
        localDevices = new ArrayList<RemoteDevice>(controller.getRemotes());
        int selection = hosts.getSelectedIndex();
        hosts.clearSelection();
        String[] labels = new String[localDevices.size()];
        for (int i = 0; i < localDevices.size(); i++)
        {
            labels[i] = localDevices.get(i).getDisplayString();
        }
        hosts.setListData(labels);
        hosts.setSelectedIndex(selection);
    }

    private void log(String message)
    {
        logWindow.append(message + '\n');
    }

    @Override
    public void discoveryStarted()
    {
        initializeUI();
        log("Service started");
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

    @Override
    public void onNewInboundTransfer(ResourceTransferClientController rtcc)
    {
        int answer = JOptionPane.showOptionDialog(null, "Do you want to accept the file?", "Inbound Transfer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        rtcc.addListener(this);
        if (answer == 0)
            rtcc.start();
        else
            rtcc.stop();
    }

    @Override
    public void transferStarted(File resource)
    {
        log("Transfer started: " + resource.getName());
    }

    @Override
    public void transferSuccess(File resource)
    {
        log("Transfer completed: " + resource.getName());
    }

    @Override
    public void transferFail(File resource)
    {
        log("Transfer failed: " + resource.getName());
    }

    @Override
    public void updateReceivedAmount(long numBytes)
    {
        log(String.format("Transfered %d bytes", numBytes));
    }

    @Override
    public void fatalUnableToParseAddress(String addressAndPort)
    {
        log("Failed to parse address " + addressAndPort);
    }

    @Override
    public void fatalUnableToOpenSocket()
    {
        log("Failed to open socket");
    }

    @Override
    public void fatalUnableToGetOutputStreamOfSocket()
    {
        log("Fatal fuckup");
    }
}
