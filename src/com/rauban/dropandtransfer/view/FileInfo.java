package com.rauban.dropandtransfer.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.rauban.dropandtransfer.controller.SessionController;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.fourthline.cling.model.meta.RemoteDevice;

import com.rauban.dropandtransfer.model.protocol.FileTransfer.ResourceHeader;
import com.rauban.dropandtransfer.view.listener.FileTransferListener;
import com.rauban.dropandtransfer.view.listener.SessionListener;




public class FileInfo extends JFrame{
	private JPanel acceptpanel;
	
	public FileInfo(String message){
		final JPanel acceptPanel = new JPanel();
		setTitle("FileInfo");
		setSize(800, 600);
		setLocationRelativeTo(null);
		JTextField outPutText = new JTextField(message, 20);
		acceptPanel.add(outPutText); 
		this.add(acceptpanel);
		JButton exitFile = new JButton("Exit program");
		exitFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				}
		});
		acceptPanel.add(exitFile);
		JButton backFile = new JButton("Back");
		backFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptPanel.setVisible(false);
				
				}
		});
		acceptPanel.add(exitFile);
		
	} 
	
	
	
}
