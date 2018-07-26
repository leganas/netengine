import by.legan.library.netengine.controller.NetClientController;
import by.legan.library.netengine.controller.NetServerController;
import by.legan.library.netengine.network.client.AbstractClient;
import by.legan.library.netengine.network.packeges.clientTOserver.ClientMessage;
import by.legan.library.netengine.network.packeges.serverTOclient.ServerMessage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetEngineTest {

    static InetAddress inet;
    static InetAddress[] ips;

    @BeforeClass
    public static void init(){
        System.out.println("TEST : Init");
        try {
            inet = InetAddress.getLocalHost();
            ips = InetAddress.getAllByName(inet.getCanonicalHostName());
            if (ips  != null ) {
                for (int i = 0; i < ips.length; i++) {
                    System.out.println(ips[i]);
                }
            }
            assertTrue(inet != null);
            assertTrue(ips != null);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    static NetServerController netServerController;
    static NetClientController netClientController;

    // Проверка конекта
    @Test(timeout = 10000)
    public void a_controller_Interaction_test() {

        System.out.println("TEST : Start server");
        netServerController = new NetServerController("Server",null);
        assertEquals(true,netServerController.start());
        System.out.println("TEST : Start server successful");


        System.out.println("TEST : Start client");
        netClientController = new NetClientController("127.0.0.1");
        netClientController.start();

        do {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Wait connected from the server ");
        } while (netClientController.client.getStatus() != AbstractClient.NetClientStatus.online);

        assertEquals(netClientController.client.getStatus(), AbstractClient.NetClientStatus.online);
        System.out.println("TEST : Connect client server successful");

    }

    @Test(timeout = 10000)
    public void b_client_message_send_to_server_test(){
        System.out.println("TEST : Client_Message send to server test");
        ClientMessage.RequestServerInfo msg = new ClientMessage.RequestServerInfo();
        netClientController.addClientMessageToQuery(msg);
        final Boolean[] flag = {false};
        netClientController.setGuiListener(message -> {
            ServerMessage.ReturnServerInfo response = (ServerMessage.ReturnServerInfo) message;
            System.out.println("TEST : Response name sender : " + response.getName());
            flag[0] = true;
        });
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (flag[0] != true);
        assertEquals(flag[0], true);
    }

    @AfterClass
    public static void off(){
        // Тушим клиента и сервер
        System.out.println("TEST : Shut down client and server");

        if (netClientController != null) netClientController.stop();
        if (netServerController != null) netServerController.stop();
    }

}
