package by.legan.library.netengine.controller;

import by.legan.library.netengine.controller.eventManager.ClientEventManager;
import by.legan.library.netengine.interfaces.Manager;
import by.legan.library.netengine.interfaces.ProgramController;
import by.legan.library.netengine.interfaces.WorkData;
import by.legan.library.netengine.network.client.AbstractClient;
import by.legan.library.netengine.network.client.NetClient;
import by.legan.library.netengine.network.packeges.clientTOserver.ClientMessage;
import by.legan.library.netengine.network.packeges.serverTOclient.ServerMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by AndreyLS on 08.02.2017.
 */
public class NetClientController extends ProgramController<WorkData> implements AbstractClient.NetClientListener, Manager.ManagerListener {
    @Target(value= ElementType.TYPE)
    @Retention(value= RetentionPolicy.RUNTIME)
    public @interface NetworkPrameters {
        enum Protocol {
            TCP,
            UDP
        }
        Protocol protocol();
        int port();
        String host();
    }

    @Target(value= ElementType.METHOD)
    @Retention(value= RetentionPolicy.RUNTIME)
    public @interface PortUDP {
        int portUDP();
    }

    public interface GUIListener {
        public void GUIMessage(Object msg);
    }

    public NetClient client;
    public String host;
    ClientEventManager clientEventManager;
    ArrayList<ClientMessage> sendQuery;
    GUIListener guiListener;


    public GUIListener getGuiListener() {
        return guiListener;
    }

    public void setGuiListener(GUIListener guiListener) {
        this.guiListener = guiListener;
    }

    public NetClientController(String IP) {
        this(null,IP);
    }

    public NetClientController(String name, String IP) {
        super(name);
        host = IP;
        client = new NetClient(this);
        client.setNetClientListener(this);
        clientEventManager = new ClientEventManager(this);
        clientEventManager.setListener(this);
        sendQuery = new ArrayList<>();
    }

    public void start(){
        client.connect();
        startUpdateThread();
    }

    public void  stop(){
        dispose();
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
                    client.sendToTCP(sendQuery.get(i));
                }
                sendQuery.clear();
            }
        }
    }

    @Override
    public void update() {
        sendAllQueryToServer();
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
        // Если в результате выполения команды/сообщения от сервака сформирован ответ то отправим его нахуй GUI
        if (guiListener != null) guiListener.GUIMessage(msg);
        if (msg instanceof ClientMessage) {
            addClientMessageToQuery((ClientMessage) msg);
        }
    }
}
