package com.rauban.dropandtransfer.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private String updateMessage; 
	private JPanel acceptPanel;

	public FileWindow(final TransferOffer to, final SessionController sc, final boolean isSending) {
		this.to = to;
		this.sc=sc; 
		setTitle("FileWindow - " + (isSending ? "Sending" : "Receiving"));
		setSize(800, 600);
		setLocationRelativeTo(null);
		panel = new JPanel();
		this.add(panel);
		sc.addListener(this);
		List<ResourceHeader> resourceList = to.getResourcesList();
		StringBuilder sb = new StringBuilder();
		long size = 0;
		for( ResourceHeader rh : to.getResourcesList()) {
			size += rh.getSize();
			sb.append(String.format("%s : %d kb\n",rh.getResourceName(), rh.getSize() >> 10));
		}
		fileInfo = new JTextArea(sb.toString(), 30, 30);
		fileNumber = new JTextField("Number of files: " + to.getResourcesCount()  ,20);
		fileSize = new JTextField("Total file size: " +  (size >> 10) + "KB" , 20);
		panel.add(fileInfo);
		panel.add(fileNumber);
		panel.add(fileSize);

		final JFrame me = this;
		JButton acceptFile = new JButton("Accept");
		acceptFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isSending)
					sc.sendTransferOffer(to);
				else
					sc.sendTransferResponse(to.getOfferId(),true);
				me.dispatchEvent(new WindowEvent(me, WindowEvent.WINDOW_CLOSING));
			}
		});
		panel.add(acceptFile);

		JButton cancelFile = new JButton("Cancel");
		cancelFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isSending)
					sc.sendTransferResponse(to.getOfferId(), false);
				me.dispatchEvent(new WindowEvent(me, WindowEvent.WINDOW_CLOSING));
			}
		});
		panel.add(cancelFile);
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
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	@Override
	public void sessionFileTransferStatusUpdate(long offerId,
			String currentFile, float currentSpeedInKBytesPerSecond,
			float currentAvgSpeedInKBytesPerSecond, float currentFilePercent,
			float currentTransferPercent) {

	}
}