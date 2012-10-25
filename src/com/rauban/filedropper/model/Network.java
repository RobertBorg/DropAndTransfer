package com.rauban.filedropper.model;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Random;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.Icon;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.registry.RegistrationException;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

public class Network extends Observable{
	private RegistryListener listener;
	private UpnpService upnpService;
	private ServerSocket ss;
	private Thread fileTransferServerThread;
	private FileTransferServer fileTransferServer;
	public Network(){
		try {
			ss = new ServerSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listener = new RegistryListener() {

            public void remoteDeviceDiscoveryStarted(Registry registry,
                                                     RemoteDevice device) {
                System.out.println(
                        "Discovery started: " + device.getDisplayString()
                );
            }

            public void remoteDeviceDiscoveryFailed(Registry registry,
                                                    RemoteDevice device,
                                                    Exception ex) {
                System.out.println(
                        "Discovery failed: " + device.getDisplayString() + " => " + ex
                );
            }

            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                System.out.println(
                        "Remote device available: " + device.getDisplayString() + " " + device.getDetails().getPresentationURI() 
                );
            }

            public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
                System.out.println(
                        "Remote device updated: " + device.getDisplayString()
                );
            }

            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                System.out.println(
                        "Remote device removed: " + device.getDisplayString()
                );
            }

            public void localDeviceAdded(Registry registry, LocalDevice device) {
                System.out.println(
                        "Local device added: " + device.getDisplayString()
                );
            }

            public void localDeviceRemoved(Registry registry, LocalDevice device) {
                System.out.println(
                        "Local device removed: " + device.getDisplayString()
                );
            }

            public void beforeShutdown(Registry registry) {
                System.out.println(
                        "Before shutdown, the registry has devices: "
                        + registry.getDevices().size()
                );
            }

            public void afterShutdown() {
                System.out.println("Shutdown of registry complete!");

            }
        };
	}
	public void stop() {
		upnpService.shutdown();
		fileTransferServer.die();
		fileTransferServerThread.interrupt();
		try {
			fileTransferServerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void start() {
		System.out.println("Starting Cling...");
        upnpService = new UpnpServiceImpl(listener);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                upnpService.shutdown();
            }
        });

        
        try {
			upnpService.getRegistry().addDevice(
			        createDevice()
			);
		} catch (RegistrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocalServiceBindingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        fileTransferServerThread = new Thread(fileTransferServer);
        fileTransferServerThread.start();
	}
	private LocalDevice createDevice()
	        throws ValidationException, LocalServiceBindingException, IOException {

	    DeviceIdentity identity =
	            new DeviceIdentity(
//	                    UDN.uniqueSystemIdentifier("Demo Binary Light")
	            		UDN.uniqueSystemIdentifier(Long.toString((new Random()).nextLong()))
	            );

	    DeviceType type =
	            new UDADeviceType("BinaryLight", 1);

	    DeviceDetails details = null;
		try {
			details = new DeviceDetails(
			        "Friendly Binary Light",
			        new ManufacturerDetails("ACME"),
			        new ModelDetails(
			                "BinLight2000",
			                "A demo light with on/off switch.",
			                "v1"
			        ),
			        new URI(String.format("%s:%d",getLocalAddress(),ss.getLocalPort()))
			);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    Icon icon = null;
	            new Icon(
	                    "image/png", 48, 48, 8,new File("assets/icon.png"));

	    LocalService switchPowerService = null;

	    return new LocalDevice(identity, type, details, icon, switchPowerService);

	    
	}
	private Object getLocalAddress() {
		// TODO Auto-generated method stub
		return null;
	}
	public void search() {
		System.out.println("searching...");
		upnpService.getControlPoint().search(new STAllHeader());		
	}
}

