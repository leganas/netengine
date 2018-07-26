package by.legan.library.netengine.network.packeges.clientTOserver;


import by.legan.library.netengine.controller.NetServerController;
import by.legan.library.netengine.interfaces.Event;
import by.legan.library.netengine.interfaces.Logs;
import by.legan.library.netengine.interfaces.Message;
import by.legan.library.netengine.network.packeges.serverTOclient.ServerMessage;

public abstract  class ClientMessage extends Message<NetServerController> {

	public static class ClientDisconnect extends ClientMessage {
		public int id;

		public ClientDisconnect(int id) {
			this.id = id;
		}

		@Override
		public Event ResponseMessage(NetServerController controller, int id) {
			Logs.out("User : " + id + " disconnected");
			return this;
		}
	}

	public static class RequestServerInfo extends ClientMessage{
		public RequestServerInfo() {}

		@Override
		public Event ResponseMessage(NetServerController controller, int id) {
			Logs.out("Получен запрос серверной информации, формируем ответ");
			ServerMessage.ReturnServerInfo msg = new ServerMessage.ReturnServerInfo();
			msg.setName(controller.getName());
			msg.setId(id); // какому клиенту послать
			return msg;
		}
	}

    public static class NewClientConnect extends ClientMessage{
		@Override
		public Event ResponseMessage(NetServerController controller, int id) {
			return this;
		}
	}
}