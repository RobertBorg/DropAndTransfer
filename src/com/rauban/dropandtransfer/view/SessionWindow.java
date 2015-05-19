package com.rauban.dropandtransfer.view;

import com.rauban.dropandtransfer.controller.SessionController;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.dropandtransfer.view.listener.SessionListener;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

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
    private JPanel fileTransfers;
    private boolean disconnected = false;

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
        panel.setLayout(new MigLayout());

        chatLog = new JTextArea("Hi, welcome to chat", 20, 20);
        chatInput = new JTextField("Enter text", 20);

        JButton sendChat = new JButton("Send Chat");
        sendChat.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String text = chatInput.getText();
                if (text != null && text.length() != 0)
                {
                    chatLog.append('\n' + "You:" + text);
                    sessionController.sendChat(text);
                }
            }
        });

        JButton selectFileButton = new JButton("Send Files");
        selectFileButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                File f = new File("Desktop");
                chooser.setCurrentDirectory(f);
                chooser.setMultiSelectionEnabled(true);
                int returnVal = chooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    TransferOffer to = sessionController.createTransferOffer(chooser.getSelectedFiles());
                    fileWindow = new FileWindow(to, sessionController, true);
                    fileWindow.setVisible(true);
                }

            }
        });
        fileTransfers = new JPanel();
        fileTransfers.setLayout(new MigLayout("wrap 1"));
        JScrollPane sp = new JScrollPane(fileTransfers);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(chatLog, "cell 0 0 3 3");
        panel.add(chatInput, "cell 0 3 3 1");
        panel.add(sendChat, "cell 3 3 1 1");
        panel.add(sp, "cell 3 0 3 3");
        panel.add(selectFileButton, "cell 4 3 1 1");

        this.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {

            }

            @Override
            public void windowClosing(WindowEvent e)
            {
                if (!disconnected)
                    sessionController.sendDisconnectRequest();
            }

            @Override
            public void windowClosed(WindowEvent e)
            {

            }

            @Override
            public void windowIconified(WindowEvent e)
            {

            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {

            }

            @Override
            public void windowActivated(WindowEvent e)
            {

            }

            @Override
            public void windowDeactivated(WindowEvent e)
            {

            }
        });
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
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                fileWindow = new FileWindow(TO, sessionController, false);
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
        fileTransfers.add(new FileTransferView(ft, sessionController));
        this.revalidate();
    }

    @Override
    public void sessionFileTransferStopped(FileTransfer ft)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionDisconnected()
    {
        disconnected = true;
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
