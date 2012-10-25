package com.rauban.dropandtransfer.main;
import java.util.Observable;
import java.util.Observer;


import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.widgets.Table;

import com.rauban.dropandtransfer.controller.DiscoveryHandler;
import com.rauban.dropandtransfer.model.Network;


public class Main extends ApplicationWindow implements Observer {
	
	//controllers
	private DiscoveryHandler nh;
	//gui elements
	private List availableHosts;
	/**
	 * Create the application window.
	 */
	public Main(DiscoveryHandler nh) {
		super(null);
		setShellStyle(SWT.DIALOG_TRIM);
		this.nh = nh;
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		setStatus("");
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new RowLayout(SWT.HORIZONTAL));
		{
			availableHosts = new List(container, SWT.BORDER);
		}
		{
			Button btnSearch = new Button(container, SWT.NONE);
			btnSearch.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					nh.search();
				}
			});
			btnSearch.setText("search");
		}
		{
			Button btnStart = new Button(container, SWT.NONE);
			btnStart.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					nh.start();
				}
			});
			btnStart.setText("start");
		}
		{
			Button btnStop = new Button(container, SWT.NONE);
			btnStop.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					nh.stop();
				}
			});
			btnStop.setText("stop");
		}

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Network model = new Network();
			DiscoveryHandler nh = new DiscoveryHandler(model);
			Main window = new Main(nh);
			window.setBlockOnOpen(true);
			model.addObserver(window);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("FileDropper");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
