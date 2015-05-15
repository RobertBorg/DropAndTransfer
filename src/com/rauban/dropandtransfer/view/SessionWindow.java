package com.rauban.dropandtransfer.view;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rauban.dropandtransfer.controller.SessionController;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.dropandtransfer.view.listener.SessionListener;

import org.fourthline.cling.model.meta.RemoteDevice;

import javax.swing.*;


public class SessionWindow extends JFrame implements SessionListener
{
    /**
     *
     */
    private static final long serialVersionUID = 454344809008334413L;
    private SessionController sessionController;
    private JPanel panel;
    private JTextArea chatLog;
    private JTextField chatInput;
    private FileWindow fileWindow;

    public SessionWindow(SessionController sc)
    {
        this.sessionController = sc;
        sc.addListener(this);
        setTitle("FileDropper - Session");
        setSize(800, 600);
        setLocationRelativeTo(null);
        panel = new JPanel();
        this.add(panel);
        initUI();
    }

    private void initUI()
    {

        panel.removeAll();
        //        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridBagLayout());

        chatLog = new JTextArea("Hi, welcome to chat", 20, 20);
        panel.add(chatLog);
        chatInput = new JTextField("Enter text", 20);
        panel.add(chatInput);

        JButton sendChat = new JButton("Send Chat");
        sendChat.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = chatInput.getText();
                if (text != null && text.length() != 0) {
                    chatLog.append('\n' + "You:" + text);
                    sessionController.sendChat(text);
                }
            }
        });
        panel.add(sendChat);

        JButton selectFileButton = new JButton("Send Files");
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

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                	TransferOffer to = sessionController.createTransferOffer(chooser.getSelectedFiles());
                	fileWindow = new FileWindow(to, sessionController);
                    fileWindow.setVisible(true);
                	sessionController.sendTransferOffer(to);
                }
                
            }
        });
        panel.add(selectFileButton);

    }

    @Override
    public void sessionConnected()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionGotOffer(TransferOffer to)
    {
        // TODO Auto-generated method stub
    	//Start a new window?
        final TransferOffer TO = to;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fileWindow = new FileWindow(TO, sessionController);
                fileWindow.fileReceiver();
                fileWindow.setVisible(true);
            }
        });

    	
    	
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
        chatLog.append('\n' + "Them:" + message);
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
}
