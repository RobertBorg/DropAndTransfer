package com.rauban.dropandtransfer.model;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rauban.dropandtransfer.controller.SessionController;
import com.rauban.dropandtransfer.view.listener.NetworkListener;
import com.rauban.dropandtransfer.view.listener.SessionServerListener;
import com.rauban.speaker_listener_pattern.speaker.AudienceHolder;
import com.rauban.speaker_listener_pattern.speaker.Speaker;


public class SessionServer implements Runnable, Speaker<SessionServerListener>, SessionServerListener {
	private ServerSocket ss;
	private boolean isRunning;
	private String baseDownloadDir;
	private int port;
	
	private ArrayList<Session> sessions;
	private ExecutorService es;
	private AudienceHolder audience;
	protected SessionServer(String baseDownloadDir){
		es = Executors.newCachedThreadPool();
		this.baseDownloadDir = baseDownloadDir;
		audience = new AudienceHolder();
		sessions = new ArrayList<Session>();
	}
	@Override
	public void run() {
		isRunning = true;
		try {
			ss = new ServerSocket(0);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		port = ss.getLocalPort();
		Socket s = null;
		while(isRunning) {
			try {
				s = ss.accept();
			}catch(SocketException se){
				//silently ignore
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if( s != null){
				handleConnection(s);
			}
		}
	}
	private void handleConnection(Socket s) {
		Session session = new Session(s);
		sessions.add(session);
		sessionAdded(new SessionController(session));
		es.submit(session);
		
		
	}
	public void die() {
		isRunning = false;
		while(!ss.isClosed()){
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		es.shutdown();
	}
	public int getPort() {
		return port;
	}
	public String getLocalAddress() {
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void addListener(SessionServerListener arg0) {
		audience.addToAudience(arg0, SessionServerListener.class);
	}
	public boolean startSession(URI uri) {
		if(uri == null)
			return false;
		try {
			Socket s = new Socket(uri.getHost(), uri.getPort());
			Session ss = new Session(s);
			sessions.add(ss);
			sessionAdded(new SessionController(ss));
			es.submit(ss);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	@Override
	public void sessionAdded(SessionController s) {
		List<SessionServerListener> ssll = audience.getAudience(SessionServerListener.class);
		for(SessionServerListener ssl : ssll) {
			ssl.sessionAdded(s);
		}
		
	}
	@Override
	public void sessionRemoved(SessionController s) {
		// TODO Auto-generated method stub
		
	}

}
