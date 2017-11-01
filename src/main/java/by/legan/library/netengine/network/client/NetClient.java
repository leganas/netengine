
package by.legan.library.netengine.network.client;


import by.legan.library.netengine.controller.NetClientController;
import by.legan.library.netengine.interfaces.Logs;
import by.legan.library.netengine.network.Network;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class NetClient extends AbstractClient{
	ClientInfo info;
	Client client;

	public NetClient(NetClientController clientGameController) {
		super(clientGameController);
		client = new Client(256000,256000);
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		client.addListener(new Listener() {
			public void connected (Connection connection) {
//				GeneralMessages.RegisterName registerName = new GeneralMessages.RegisterName();
//				registerName.name = name;
//				client.sendTCP(registerName);
				info = new ClientInfo();
				info.UserID = client.getID();
				status = NetClientStatus.online;
			}

			public void received (Connection connection, Object object) {
				netClientListener.netClientMessage(object);
			}

			public void disconnected (Connection connection) {
				Logs.out("netClient disconnected");
			}
		});

		String input = clientGameController.host;
		final String host = input.trim();

		input = "ClientName";
		name = input.trim();

		new Thread("Connect") {
			public void run () {
				try {
					client.connect(50000, host, Network.portTCP, Network.portUDP);
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
		Logs.out("netClient disconnected from the server and dispose");
	}
}
