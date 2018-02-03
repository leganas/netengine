
package by.legan.library.netengine.network.server;

import by.legan.library.netengine.controller.NetServerController;
import by.legan.library.netengine.interfaces.Logs;
import by.legan.library.netengine.network.Network;
import by.legan.library.netengine.network.packeges.clientTOserver.ClientMessage;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.util.ArrayList;


public class NetServer extends AbstractServer{
	Server server;

	public NetServer (NetServerController netServerController) {
		super(netServerController);
		server = new Server(256000,256000) {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				status = NetServerStatus.online;
				return new SConnection();
			}
		};

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);
		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				// We know all connections for this server are actually ChatConnections.
				SConnection connection = (SConnection)c;
				netServerListener.netServerMessage(connection.getID(),object);
			}

			public void disconnected (Connection c) {
				SConnection connection = (SConnection)c;
				if (connection.name != null) {
					// Announce to everyone that someone (with a registered name) has left.
//					GeneralMessages.ChatMessage chatMessage = new GeneralMessages.ChatMessage();
//					chatMessage.text = connection.name + " disconnected.";
//					server.sendToAllTCP(chatMessage);
//					updateNames();
//					ClientMessage.DisconectPlayer msg = new ClientMessage.DisconectPlayer();
//					netServerListener.netServerMessage(c.getID(), msg);
					Logs.out("User : " + c.getID() + " disconnected");
				}
			}
		});
	}

	public boolean start(){
		try {
			server.bind(Network.portTCP, Network.portUDP);
			server.start();
			return true;
		} catch (Exception e) {
			Logs.out("ERROR Server already running");
			System.exit(0);
			return false;
		}
	}
	
	public int getCollClient(){
		return server.getConnections().length;
	}

	@Override
	public void dispose(){
		server.stop();
		Logs.out("NetServer stopped and dispose");
	}

	public void sendToAllUDP(Object message){
		server.sendToAllUDP(message);
	}
	
	public void sendToAllTCP(Object message){
		server.sendToAllTCP(message);
	} 
	
	public void sendtoTCP(int connectionID, Object message){
		server.sendToTCP(connectionID, message);
	} 
	
	public void sendtoUDP(int connectionID, Object message){
		server.sendToUDP(connectionID, message);
	}

	// This holds per connection state.
	static class SConnection extends Connection {
		public String name;
	}
}
