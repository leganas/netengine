
package by.legan.library.netengine.network;

import by.legan.library.netengine.network.packeges.clientTOserver.ClientMessage;
import by.legan.library.netengine.network.packeges.serverTOclient.ServerMessage;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

// This class is a convenient place to keep things common to both the client and server.
public class Network {

	static public int portTCP = 8900;
	static public int portUDP = 8902;

	public static void setPortTCP(int portTCP) {
		Network.portTCP = portTCP;
	}
	public static void setPortUDP(int portUDP) {
		Network.portUDP = portUDP;
	}
	public static int getPortTCP() {
		return portTCP;
	}
	public static int getPortUDP() {
		return portUDP;
	}

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(float[].class);
		kryo.register(int[].class);
		kryo.register(Object.class);
		kryo.register(String[].class);
		kryo.register(Object[].class);
		kryo.register(ArrayList.class);

		kryo.register(ClientMessage.class);
		kryo.register(ClientMessage.RequestServerInfo.class);

		kryo.register(ServerMessage.class);
		kryo.register(ServerMessage.ReturnServerInfo.class);
	}

}
