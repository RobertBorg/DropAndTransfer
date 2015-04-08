package com.rauban.dropandtransfer.model;

import com.rauban.dropandtransfer.view.listener.NetworkListener;
import com.rauban.speaker_listener_pattern.speaker.AudienceHolder;
import com.rauban.speaker_listener_pattern.speaker.Speaker;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.registry.RegistrationException;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Network implements Speaker<NetworkListener>
{
    private RegistryListener listener;
    private UpnpService upnpService;
    private Thread fileTransferServerThread;
    private ResourceTransferServer fileTransferServer;

    private NetworkState state;
    private AudienceHolder audience;

    public Network()
    {
        audience = new AudienceHolder();
        state = new NetworkState();
        listener = new RegistryListener()
        {

            public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device)
            {
                //This method does the same as remoteDeviceAdded and should be ignored
            }

            public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex)
            {
                System.err.println("Discovery failed: " + device.getDisplayString() + " => " + ex);
            }

            public void remoteDeviceAdded(Registry registry, RemoteDevice device)
            {
                if (!state.remoteDevices.contains(device))
                {
                    state.remoteDevices.add(device);
                    List<NetworkListener> nll = audience.getAudience(NetworkListener.class);
                    for (NetworkListener nl : nll)
                    {
                        nl.discoveryRemoteDeviceAdded(device);
                    }
                }
            }

            public void remoteDeviceUpdated(Registry registry, RemoteDevice device)
            {
                if (state.remoteDevices.remove(device))
                {
                    //XXX should we care?
                }
                state.remoteDevices.add(device);
                //XXX update view
            }

            public void remoteDeviceRemoved(Registry registry, RemoteDevice device)
            {
                if (state.remoteDevices.remove(device))
                {
                    //XXX should we care?
                }
                //XXX update view
            }

            public void localDeviceAdded(Registry registry, LocalDevice device)
            {
                //This is weirdly the method that's called once the discovery is online
                List<NetworkListener> nll = audience.getAudience(NetworkListener.class);
                for (NetworkListener nl : nll)
                {
                    nl.discoveryStarted();
                }
            }

            public void localDeviceRemoved(Registry registry, LocalDevice device)
            {
                // XXX should we care?
            }

            public void beforeShutdown(Registry registry)
            {
                state.clear();
                //XXX update view
            }

            public void afterShutdown()
            {
                System.out.println("Shutdown of registry complete!");

            }
        };
    }

    public void stop()
    {
        upnpService.shutdown();
        fileTransferServer.die();
        fileTransferServerThread.interrupt();
        try
        {
            fileTransferServerThread.join();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void start()
    {
        fileTransferServer = new ResourceTransferServer("./Received");
        fileTransferServerThread = new Thread(fileTransferServer);
        fileTransferServerThread.start();

        upnpService = new UpnpServiceImpl(listener);
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                upnpService.shutdown();
            }
        });

        try
        {
            upnpService.getRegistry().addDevice(createDevice());
        }
        catch (RegistrationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (LocalServiceBindingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ValidationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @SuppressWarnings("rawtypes")
    private LocalDevice createDevice() throws ValidationException, LocalServiceBindingException, IOException
    {

        DeviceIdentity identity = new DeviceIdentity(
                UDN.uniqueSystemIdentifier(Long.toString((new Random()).nextLong())));

        DeviceType type = new UDADeviceType("DropNTransfer", 1);

        DeviceDetails details = null;
        try {
			details = new DeviceDetails(System.getProperty("user.name"), new ManufacturerDetails("EDA095"), new ModelDetails("DropNTransfer"), new URI(null, null, fileTransferServer.getLocalAddress(), fileTransferServer.getPort(), null, null, null));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Icon icon = null;
        new Icon("image/png", 48, 48, 8, new File("assets/icon.png"));

        return new LocalDevice(identity, type, details, icon, (LocalService) null);

    }

    public void search()
    {
        upnpService.getControlPoint().search(new STAllHeader());
    }

    public static class NetworkState implements Cloneable
    {
        public LinkedList<RemoteDevice> remoteDevices;

        public NetworkState()
        {
            remoteDevices = new LinkedList<RemoteDevice>();
        }

        public void clear()
        {
            remoteDevices.clear();
        }

        @SuppressWarnings("unchecked")
        protected Object clone()
        {
            NetworkState clone = new NetworkState();
            clone.remoteDevices = (LinkedList<RemoteDevice>) this.remoteDevices.clone(); //XXX only uses shallow copy
            return clone;
        }
    }

    public ResourceTransferAbstractClient transferResource(String connectionAddress, String... paths)
    {
        ResourceTransferAbstractClient ftc = new ResourceTransferOutboundClient(connectionAddress, paths);

        return ftc;
    }

    public List<RemoteDevice> getRemotes()
    {
        return state.remoteDevices;
    }

    @Override
    public void addListener(NetworkListener arg0)
    {
        audience.addToAudience(arg0, NetworkListener.class);
    }

}

