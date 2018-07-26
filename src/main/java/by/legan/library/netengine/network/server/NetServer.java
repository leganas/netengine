
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
				status = NetServerStatus.online;
				SConnection connection = new SConnection();
				connection.name = "client";
				netServerListener.netServerMessage(connection.getID(),new ClientMessage.NewClientConnect());
				return connection;
			}
		};

		Network.register(server, netServerController.classArrayList);
		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				// Отправляем на уровень NetServerController сообщение пришедшее от клиента
				SConnection connect = (SConnection)connection;
				netServerListener.netServerMessage(connect.getID(),object);
			}

			public void disconnected (Connection connection) {
				// Оповещаем NetServerController о том что клиент отключился
				SConnection conn = (SConnection)connection;
				netServerListener.netServerMessage(conn.getID(),new ClientMessage.ClientDisconnect(conn.getID()));
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
			return false;
		}
	}
	
	public int getCollClient(){
		return server.getConnections().length;
	}


	public Connection[] getConnections(){
		return server.getConnections();
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
