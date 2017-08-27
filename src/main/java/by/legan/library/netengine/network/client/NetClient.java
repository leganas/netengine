
package by.legan.library.netengine.network.client;


import by.legan.library.netengine.Status;
import by.legan.library.netengine.controller.ClientController;
import by.legan.library.netengine.network.Network;
import by.legan.library.netengine.network.packeges.GeneralMessages;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class NetClient extends AbstractClient{
	Client client;

	public NetClient(ClientController clientGameController) {
		super(clientGameController);
	}

	public NetClient (final ClientController clientGameController, String myhost) {
		super(clientGameController);
		client = new Client(256000,256000);
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		client.addListener(new Listener() {
			public void connected (Connection connection) {
				GeneralMessages.RegisterName registerName = new GeneralMessages.RegisterName();
				registerName.name = name;
				client.sendTCP(registerName);
				ClientInfo.UserID = client.getID();
				Status.clientStatus = Status.ClientStatus.online;
			}

			public void received (Connection connection, Object object) {
				netClientListener.netClientMessage(object);
			}

			public void disconnected (Connection connection) {
//				Gdx.app.log("LGame", "netClient server down");
			}
		});

		String input = myhost;
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
	public void sendtoTCP(Object message) {
		sendMessage(message);
	}

	public  void sendMessage(Object message){
	try {
		client.sendTCP(message);
	} catch (Exception e) {
	}
	}

	@Override
	public void dispose() {
		client.stop();
//		Gdx.app.log("LGame", "netClient disconect from the server and dispose");
	}
}
