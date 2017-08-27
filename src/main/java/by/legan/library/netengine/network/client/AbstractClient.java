package by.legan.library.netengine.network.client;


import by.legan.library.netengine.controller.ClientController;
import by.legan.library.netengine.interfaces.Disposable;
import by.legan.library.netengine.interfaces.Net;

public abstract class AbstractClient extends Net<ClientController> implements Disposable {
	String name;

	public AbstractClient(ClientController clientGameController) {
		super(clientGameController);
	}

	public interface NetClientListener {
		public void netClientMessage(Object event);
	}

	NetClientListener netClientListener;


	public void setNetClientListener(NetClientListener netClientListener) {
		this.netClientListener = netClientListener;
	}

	public void sendMSGtoListener(Object event) {
		netClientListener.netClientMessage(event);
	}

	public abstract void sendtoTCP(Object message);

	public void sendMessage(Object message) {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
