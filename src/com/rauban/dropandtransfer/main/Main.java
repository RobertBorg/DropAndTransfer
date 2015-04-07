package com.rauban.dropandtransfer.main;

import com.rauban.dropandtransfer.view.MainWindow;

import com.rauban.dropandtransfer.controller.NetworkController;
import com.rauban.dropandtransfer.model.Network;

public class Main  {
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Network model = new Network();
			NetworkController nh = new NetworkController(model);
			MainWindow mainWindow = new MainWindow(nh);
			mainWindow.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
