
package by.legan.library.netengine.network;

import by.legan.library.netengine.NetClientCard;
import by.legan.library.netengine.UserCard;
import by.legan.library.netengine.WorkData;
import by.legan.library.netengine.network.packeges.GeneralMessages;
import by.legan.library.netengine.network.packeges.clientTOserver.ClientMessage;
import by.legan.library.netengine.network.packeges.serverTOclient.ServerMessage;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
	static public final int portTCP = 8900;
	static public final int portUDP = 8902;

	
	
	public static int getPorttcp() {
		return portTCP;
	}

	public static int getPortudp() {
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
		kryo.register(WorkData.class);
		kryo.register(NetClientCard.class);
		kryo.register(NetClientCard.Possition.class);
		kryo.register(UserCard.Permission.class);


		kryo.register(GeneralMessages.RegisterName.class);
		kryo.register(GeneralMessages.UpdateNames.class);
		kryo.register(GeneralMessages.ChatMessage.class);
		
		kryo.register(ClientMessage.class);
		kryo.register(ClientMessage.RequestServerInfo.class);
		kryo.register(ClientMessage.DisconectPlayer.class);
		kryo.register(ClientMessage.RequestAutorization.class);
		kryo.register(ClientMessage.RequestWorkData.class);

		kryo.register(ServerMessage.class);
		kryo.register(ServerMessage.ReturnServerInfo.class);
		kryo.register(ServerMessage.ReturnAutorizationStatus.class);
		kryo.register(ServerMessage.ReturnWorkData.class);
	}

}
