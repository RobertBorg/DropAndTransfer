package com.rauban.dropandtransfer.view;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.rauban.dropandtransfer.controller.SessionController;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.dropandtransfer.view.listener.SessionListener;

public class SessionWindow extends JFrame implements SessionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 454344809008334413L;
	private SessionController sessionController;
	private JPanel panel;
	private JTextArea chatLog;
	private JTextField chatInput;
	
	public SessionWindow(SessionController sc) {
		this.sessionController = sc;
		sc.addListener(this);
		setTitle("FileDropper - Session");
		setSize(800, 600);
		setLocationRelativeTo(null);
		panel = new JPanel();
		this.add(panel);
		initUI();
	}
	
	private void initUI() {
	
		panel.removeAll();
//        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridBagLayout());
        
        chatLog = new JTextArea();
        panel.add(chatLog);
        chatInput = new JTextField();
        panel.add(chatInput);
        
        JButton sendChat = new JButton("Send");
        sendChat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = chatInput.getText();
				if(text != null && text.length() != 0){					
					chatLog.append('\n' + "You:" + text);
					sessionController.sendChat(text);
				}
			}
		});
        panel.add(sendChat);
        
	}

	@Override
	public void sessionConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionGotOffer(TransferOffer to) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionGotResponse(long offerId, boolean accept) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionGotOfferCancel(long offerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionGotChat(String message) {
		// TODO Auto-generated method stub
		chatLog.append('\n' + "Them:" + message);
	}

	@Override
	public void sessionFileTransferStarted(FileTransfer ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionFileTransferStopped(FileTransfer ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionDisconnected() {
		// TODO Auto-generated method stub
		
	}
}
