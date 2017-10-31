package by.legan.library.netengine.controller;

import by.legan.library.netengine.Assets;
import by.legan.library.netengine.Setting;
import by.legan.library.netengine.Status;
import by.legan.library.netengine.WorkData;
import by.legan.library.netengine.controller.eventManager.ClientEventManager;
import by.legan.library.netengine.interfaces.Manager;
import by.legan.library.netengine.interfaces.ProgController;
import by.legan.library.netengine.network.client.AbstractClient;
import by.legan.library.netengine.network.client.NetClient;
import by.legan.library.netengine.network.packeges.clientTOserver.ClientMessage;
import by.legan.library.netengine.network.packeges.serverTOclient.ServerMessage;

import java.util.ArrayList;

/**
 * Created by AndreyLS on 08.02.2017.
 */
public class ClientController extends ProgController<WorkData> implements AbstractClient.NetClientListener, Manager.ManagerListener {
    public interface GUIListener {
        public void GUIMessage(Object msg);
    }

    public NetClient client;
    ClientEventManager clientEventManager;
    ArrayList<ClientMessage> sendQuery;
    GUIListener guiListener;

    public GUIListener getGuiListener() {
        return guiListener;
    }

    public void setGuiListener(GUIListener guiListener) {
        this.guiListener = guiListener;
    }

    public ClientController(String name) {
        super(name);
        sendQuery = new ArrayList<>();
    }

    @Override
    public void init() {
        client = new NetClient(this, Setting.IPAdressFromClient);
        client.setNetClientListener(this);
        clientEventManager = new ClientEventManager(this);
        clientEventManager.setListener(this);
        if (Assets.workData == null) Assets.workData = new WorkData();
    }

    public void addClientMessageToQuery(ClientMessage msg){
        synchronized (sendQuery) {
            sendQuery.add(msg);
        }
    }

    void sendAllQueryToServer(){
        synchronized (sendQuery) {
            if (sendQuery.size() > 0) {
                for (int i = 0; i < sendQuery.size(); i++) {
                    client.sendtoTCP(sendQuery.get(i));
                }
                sendQuery.clear();
            }
        }
    }

    @Override
    public void update() {
        if (Status.clientStatus != Status.ClientStatus.offline) {
            sendAllQueryToServer();
        }
        if (clientEventManager != null) clientEventManager.process();
    }

    @Override
    public void dispose() {
        super.dispose();
        client.dispose();
    }

    @Override
    public void netClientMessage(Object event) {
        if (event instanceof ServerMessage) {
            // если получена команда от сервера то ставим на обработку
            ServerMessage ev = (ServerMessage) event;
            clientEventManager.addEventToQueue(ev);
        }
    }

    @Override
    public void ListenerMessage(Object msg) {
        // Если в результате выполения команды/сообщения от сервака сформирован ответ то отправим его нахуй
        // Нашуму GUI и за одно назад серверу что бы он сука знал что мы получили его сообщения и действуем
        // хотя он сука и так знает что мы получили но пусть блять будет уверен в этом :)
        if (guiListener != null) guiListener.GUIMessage(msg);
        if (msg instanceof ClientMessage) {
            addClientMessageToQuery((ClientMessage) msg);
            // client.sendtoTCP(msg);
        }
    }
}
