package com.rauban.dropandtransfer.view;

import com.rauban.dropandtransfer.controller.NetworkController;
import com.rauban.dropandtransfer.controller.SessionController;
import com.rauban.dropandtransfer.controller.SessionServerController;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.dropandtransfer.view.listener.FileTransferListener;
import com.rauban.dropandtransfer.view.listener.NetworkListener;
import com.rauban.dropandtransfer.view.listener.SessionListener;
import com.rauban.dropandtransfer.view.listener.SessionServerListener;
import org.fourthline.cling.model.meta.RemoteDevice;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame implements NetworkListener, FileTransferListener, SessionListener, SessionServerListener
{
    /**
     *
     */
    private static final long serialVersionUID = 333514456134424836L;

    private NetworkController networkController;
    private SessionServerController serverSessionController;
    private SessionController sessionController;
    private SessionWindow sessionWindow;

    private boolean uiInitialized = false;

    private JPanel panel;
    JTextArea logWindow;
    private JList<String> hosts;

    private List<RemoteDevice> localDevices;

    public MainWindow(NetworkController controller)
    {
        this.networkController = controller;
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

                    if (device != null)
                    {
                        //TODO Missing port number, how do we get it?
                        //TODO send transfer offer

                        //                        transferClientController.addListener(view);
                        //                        transferClientController.start();
                    }
                }
            }
        });
        panel.add(selectFileButton);

        JButton sendMessageButton = new JButton("Connect");
        sendMessageButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (hosts.getSelectedIndex() >= 0)
                {
                    RemoteDevice device = localDevices.get(hosts.getSelectedIndex());
                    URI uri = device.getDetails().getPresentationURI();
                    serverSessionController.startSession(uri);
                    log("Starting session...");
                }
            }
        });
        panel.add(sendMessageButton);

        hosts = new JList<String>();
        hosts.setLayoutOrientation(JList.VERTICAL);
        hosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroller = new JScrollPane(hosts);
        panel.add(listScroller);
        this.updateRemotes();
        networkController.search();
        this.uiInitialized = true;
    }

    private synchronized void updateRemotes()
    {
        localDevices = new ArrayList<RemoteDevice>(networkController.getRemotes());
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

        if (logWindow != null) //TODO fix ?
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

    //    @Override
    //    public void onNewInboundTransfer(ResourceTransferClientController rtcc)
    //    {
    //        int answer = JOptionPane.showOptionDialog(null, "Do you want to accept the file?", "Inbound Transfer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
    //        rtcc.addListener(this);
    //        if (answer == 0)
    //            rtcc.start();
    //        else
    //            rtcc.stop();
    //    }

    @Override
    public void sessionServerStarted(SessionServerController ss)
    {
        // TODO Auto-generated method stub
        serverSessionController = ss;
        ss.addListener(this);
        log("got serversessioncontroller");
    }

    @Override
    public void sessionFileTransferStatusUpdate(long offerId, String currentFile, float currentSpeedInKBytesPerSecond, float currentAvgSpeedInKBytesPerSecond, float currentFilePercent,
            float currentTransferPercent)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionConnected()
    {
        // TODO Auto-generated method stub
        log("session connected");

    }

    @Override
    public void sessionGotOffer(TransferOffer to)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionGotResponse(long offerId, boolean accept)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionGotOfferCancel(long offerId)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionGotChat(String message)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionFileTransferStarted(FileTransfer ft)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionFileTransferStopped(FileTransfer ft)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionDisconnected()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionAdded(SessionController s)
    {
        // TODO Auto-generated method stub
        log("session added..");
        s.addListener(this);
        sessionController = s;
        sessionWindow = new SessionWindow(s);
        sessionWindow.setVisible(true);

    }

    @Override
    public void sessionRemoved(SessionController s)
    {
        // TODO Auto-generated method stub

    }
}
