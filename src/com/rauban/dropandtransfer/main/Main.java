package com.rauban.dropandtransfer.main;

import com.rauban.dropandtransfer.view.newui.MainWindow;
import org.eclipse.swt.widgets.Display;

import com.rauban.dropandtransfer.controller.NetworkController;
import com.rauban.dropandtransfer.model.Network;
import com.rauban.dropandtransfer.view.MainView;

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
//			MainView window = new MainView(nh);
//			window.setBlockOnOpen(true);
			//XXX update view
//			window.open();
//			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
