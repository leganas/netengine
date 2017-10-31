package by.legan.library.netengine.network.packeges.serverTOclient;


import by.legan.library.netengine.Assets;
import by.legan.library.netengine.UserCard;
import by.legan.library.netengine.WorkData;
import by.legan.library.netengine.controller.ClientController;
import by.legan.library.netengine.interfaces.Event;
import by.legan.library.netengine.interfaces.Message;
import by.legan.library.netengine.utils.Logs;

/**Сообщение которые посылает сервер клиенту*/
public abstract class ServerMessage extends Message<ClientController> {

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
		public Event ResponseMessage(ClientController controller, int id) {
			Logs.out("Получен ответ от сервера, ReturnServerInfo");
			Logs.out("Server Name : " + getName());
			// Возвращаем самого себя что бы отработал GUI
			return this;
		}
	}

	public static class ReturnAutorizationStatus extends ServerMessage{
		public boolean flag=false;
		public UserCard.Permission permission = UserCard.Permission.Guest;

		public ReturnAutorizationStatus() {
		}

		@Override
		public Event ResponseMessage(ClientController controller, int id) {
			Logs.out("Autorization : " + flag);
			Logs.out("Permission : " + permission.toString());
			return this;
		}
	}

	public static class ReturnWorkData extends ServerMessage {
		public WorkData workData;

		public WorkData getWorkData() {
			return workData;
		}

		public void setWorkData(WorkData workData) {
			this.workData = workData;
		}

		@Override
		public Event ResponseMessage(ClientController controller, int id) {
			Assets.workData = workData;
			Logs.out("Получены пользовательские данные");
			return this;
		}
	}
	
}
