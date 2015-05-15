package com.rauban.dropandtransfer.view;

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

import com.rauban.dropandtransfer.controller.SessionController;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.ResourceHeader;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;
import com.rauban.dropandtransfer.view.listener.FileTransferListener;
import com.rauban.dropandtransfer.view.listener.SessionListener;

public class FileWindow extends JFrame implements SessionListener, FileTransferListener {
	private JPanel panel;
	private TransferOffer to;
	private JTextArea fileInfo;
	private JTextField fileNumber;
	private JTextField fileSize; 
	private SessionController sc;
	private FileTransfer ft; 
	
	public FileWindow(final TransferOffer to, final SessionController sc) {
		this.to = to;
		this.sc=sc; 
		setTitle("FileWindow");
		setSize(800, 600);
		setLocationRelativeTo(null);
		panel = new JPanel();
		this.add(panel);
		sc.addListener(this);
		List<ResourceHeader> resourceList = to.getResourcesList();
		StringBuilder sb = new StringBuilder();
		long size = 0; 
		for (int i = 0; i < resourceList.size(); i++) {
			sb.append(to.getResourcesList().get(i).getResourceName()).append(
					"\n");
			size = size + to.getResourcesList().get(i).getSize(); 
		}
		fileInfo = new JTextArea("Filename: " + sb.toString(), 30, 30);
		fileNumber = new JTextField("Number of files: " + to.getResourcesCount()  ,20);
		fileSize = new JTextField("File transfere size: " +  size/1000 + "KB" , 20);
		panel.add(fileInfo);
		panel.add(fileNumber);
		panel.add(fileSize);
	}
	public void fileReceiver(){

		JButton acceptFile = new JButton("Accept");
		acceptFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sc.sendTransferResponse(to.getOfferId(), true);
				
			}
		});
		panel.add(acceptFile);
		
		JButton cancelFile = new JButton("Cancel");
		cancelFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sc.sendTransferResponse(to.getOfferId(), false);
				}
		});
		panel.add(cancelFile);
		
	}
	public void fileSender(){
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
		System.out.println("Got responce" + accept);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionGotOfferCancel(long offerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionGotChat(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionFileTransferStarted(FileTransfer ft) {
		this.ft = ft;
		ft.addListener(this);
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
	@Override
	public void sessionFileTransferStatusUpdate(long offerId,
			String currentFile, float currentSpeedInKBytesPerSecond,
			float currentAvgSpeedInKBytesPerSecond, float currentFilePercent,
			float currentTransferPercent) {
		// TODO Auto-generated method stub
		
	}
}

// sender

// receiver
// session.Controller.sendTransfe