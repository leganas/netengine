package by.legan.library.netengine.network.client;


import by.legan.library.netengine.controller.NetClientController;
import by.legan.library.netengine.interfaces.Disposable;
import by.legan.library.netengine.interfaces.Net;

/**
 * Описывает обстрактного клиента сетевого движка сука
 * */
public abstract class AbstractClient extends Net<NetClientController> implements Disposable {
	public enum NetClientStatus {
		init,
		offline,
		online
	}

	String name;

	NetClientStatus status = NetClientStatus.init;

	public AbstractClient(NetClientController clientGameController) {
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

	public abstract void sendToTCP(Object message);

	public void sendMessage(Object message) {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
