
package by.legan.library.netengine.network.client;


import by.legan.library.netengine.controller.NetClientController;
import by.legan.library.netengine.interfaces.Logs;
import by.legan.library.netengine.network.Network;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetClient extends AbstractClient{

	ClientInfo info;
	Client client;

	public NetClient(NetClientController clientGameController) {
		super(clientGameController);
		client = new Client(256000,256000);
		client.start();

		Network.register(client);

		client.addListener(new Listener() {
			public void connected (Connection connection) {
				info = new ClientInfo();
				info.UserID = client.getID();
				status = NetClientStatus.online;
			}

			public void received (Connection connection, Object object) {
				netClientListener.netClientMessage(object);
			}

			public void disconnected (Connection connection) {
				Logs.out("NetClient disconnected");
			}
		});
		if (programController.getName() == null) {
			try {
				InetAddress inet = InetAddress.getLocalHost();
				InetAddress[] ips = InetAddress.getAllByName(inet.getCanonicalHostName());
				if (ips  != null ) {
					programController.setName(inet.toString());
					name = programController.getName();
				}
			} catch (UnknownHostException e) {
				name = "UnknownHost";
                programController.setName(name);
			}
		}
		name = "Client : " + name;
        programController.setName(name);
	}

	public void connect(){
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(50000, programController.host, Network.portTCP, Network.portUDP);
				} catch (IOException ex) {
					System.exit(0);
				}
			}
		}.start();
	}

	@Override
	public void sendToTCP(Object message) {
		sendMessage(message);
	}

	public  void sendMessage(Object message){
	try {
		client.sendTCP(message);
	} catch (Exception e) {
		Logs.out(e.toString());
	}
	}

	@Override
	public void dispose() {
		client.stop();
		Logs.out("NetClient disconnected from the server and dispose");
	}
}
