import by.legan.library.netengine.controller.NetClientController;
import by.legan.library.netengine.controller.NetServerController;
import by.legan.library.netengine.network.client.AbstractClient;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerConnectionTest {

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

    @Test(timeout = 10000)
    public void controller_Interaction_test() {

        System.out.println("TEST : запускам Сервер");
        netServerController = new NetServerController("Server");
        assertEquals(true,netServerController.start());
        System.out.println("TEST : запуск сервера удачный");


        System.out.println("TEST : запускаем Клиента");
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
        System.out.println("TEST : коннект клиент сервер состоялся");

    }

    @AfterClass
    public static void off(){
        // Тушим клиента и сервер
        System.out.println("TEST : Тушим сервер и клиента");

        if (netClientController != null) netClientController.stop();
        if (netServerController != null) netServerController.stop();
    }

}
