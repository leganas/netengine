package by.legan.library.netengine.controller;

import by.legan.library.netengine.controller.eventManager.ServerEventManager;
import by.legan.library.netengine.interfaces.Logs;
import by.legan.library.netengine.interfaces.Manager;
import by.legan.library.netengine.interfaces.ProgramController;
import by.legan.library.netengine.interfaces.WorkData;
import by.legan.library.netengine.network.packeges.clientTOserver.ClientMessage;
import by.legan.library.netengine.network.packeges.serverTOclient.ServerMessage;
import by.legan.library.netengine.network.server.AbstractServer;
import by.legan.library.netengine.network.server.NetServer;
import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;

/**
 * Серверный контроллер
 * Назначение :
 * - создаёт и запускает NetServer
 * - управляет всей внутренней работой сервера
 * - Обновляет WorkDataBase , и отсылает по запросу или по таймеру копию клиенту
 */

public class NetServerController extends ProgramController<WorkData> implements AbstractServer.NetServerListener, Manager.ManagerListener {
    public interface GUIListener {
        public void GUIMessage(Object msg);
    }

    NetServer server;
    ServerEventManager serverEventManager;
    ArrayList<ServerMessage> sendQuery;
    GUIListener  guiListener;
    public ArrayList<Class> classArrayList;

    public GUIListener getGuiListener() {
        return guiListener;
    }

    public void setGuiListener(GUIListener guiListener) {
        this.guiListener = guiListener;
    }

    public NetServerController(String name, ArrayList<Class> classArrayList) {
        super(name);
        this.classArrayList = classArrayList;
        server =  new NetServer(this);
        server.setGameServerListener(this);
        serverEventManager = new ServerEventManager(this);
        serverEventManager.setListener(this);
        sendQuery = new ArrayList<>();
    }

    public boolean start(){
        startUpdateThread(getName());
        return server.start();
    }

    public void stop(){
        dispose();
    }

    public void addServerMessageToQuery(ServerMessage msg){
        synchronized (sendQuery) {
            sendQuery.add(msg);
        }
    }

    void sendAllQueryToClient(){
        synchronized (sendQuery) {
            if (sendQuery.size() > 0) {
                for (int i = 0; i < sendQuery.size(); i++) {
                    if (sendQuery.get(i).getId() == -1) {
                        server.sendToAllTCP(sendQuery.get(i));
                    } else {
                      server.sendtoTCP(sendQuery.get(i).getId(), sendQuery.get(i));}
                }
                sendQuery.clear();
            }
        }
    }

    /**
     * Получаем список всех подключенных клиентов
     * @return список клиенктов
     */
    public Connection[] getConnections () {
        return server.getConnections();
    }



    @Override
    public void update() {
        sendAllQueryToClient();
        if (status == Status.Dispose) dispose();
        // Выполняем команда в отдельном потоке сервер контроллера
        if (serverEventManager != null){
            serverEventManager.process();
        }
    }

    /**Тут мы получаем сообщения из сети от клиентов*/
    @Override
    public void netServerMessage(int connectionID, Object event) {
        if (event instanceof ClientMessage) {
            Logs.out("Server : Принято сообщение от клиента - " +event);

            // если получена команда от клиента то ставим на обработку
            // пока хуй знает куда девать connectionID
            ClientMessage ev = (ClientMessage) event;
            // Устанавливаем ID для полученого события
            // (как индификатор пользователя который нам это прислал)
            // потом при помощи него в методе ListenerMessage мы отправим именно
            // этому клиенту ответное сообщение назад
            ev.setId(connectionID);
            serverEventManager.addEventToQueue(ev);
        }
    }

    /**Тут мы получаем обратные вызовы от нашего ServerEventManager*/
    @Override
    public void ListenerMessage(Object msg) {
        if (guiListener != null) guiListener.GUIMessage(msg);
        if (msg instanceof ServerMessage) {
            Logs.out("Server : Сформирован ответ для клиента, отправляем - " +msg);
            // Получили от евент менеджера сообщение для клиента значит отправляем клиенту
            addServerMessageToQuery((ServerMessage) msg);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        server.dispose();
    }

}
