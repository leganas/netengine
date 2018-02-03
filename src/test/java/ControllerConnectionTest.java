import by.legan.library.netengine.controller.NetClientController;
import by.legan.library.netengine.controller.NetServerController;
import by.legan.library.netengine.network.client.AbstractClient;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;

public class ControllerConnectionTest {

    @Test(timeout = 10000)
    public void controller_Interaction_test() throws InterruptedException {
        try {
            InetAddress inet = InetAddress.getLocalHost();
            InetAddress[] ips = InetAddress.getAllByName(inet.getCanonicalHostName());
            if (ips  != null ) {
                for (int i = 0; i < ips.length; i++) {
                    System.out.println(ips[i]);
                }
            }
        } catch (UnknownHostException e) {

        }

        System.out.println("TEST : запускам Сервер");
        NetServerController netServerController = new NetServerController("Server");
        assertEquals(true,netServerController.start());
        System.out.println("TEST : запуск сервера удачный");


        System.out.println("TEST : запускаем Клиента");
        NetClientController netClientController = new NetClientController("127.0.0.1");
        netClientController.start();

        do {
            Thread.sleep(10);
            System.out.println("Wait connected from the server ");
        } while (netClientController.client.getStatus() != AbstractClient.NetClientStatus.online);

        assertEquals(netClientController.client.getStatus(), AbstractClient.NetClientStatus.online);
        System.out.println("TEST : коннект клиент сервер состоялся");

        System.out.println("TEST : Тушим сервер и клиента");
        // Тушим клиента и сервер
        netClientController.stop();
        netServerController.stop();
    }
}
