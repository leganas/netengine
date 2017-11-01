package by.legan.library.netengine.network.packeges.serverTOclient;


import by.legan.library.netengine.controller.NetClientController;
import by.legan.library.netengine.interfaces.Event;
import by.legan.library.netengine.interfaces.Logs;
import by.legan.library.netengine.interfaces.Message;

/**Сообщение которые посылает сервер клиенту*/
public abstract class ServerMessage extends Message<NetClientController> {

	public static class ReturnServerInfo extends ServerMessage{
		String name;

		public ReturnServerInfo() {
			super();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public Event ResponseMessage(NetClientController controller, int id) {
			Logs.out("Получен ответ от сервера, ReturnServerInfo");
			Logs.out("Server Name : " + getName());
			// Возвращаем самого себя что бы отработал GUI
			return this;
		}
	}
}
