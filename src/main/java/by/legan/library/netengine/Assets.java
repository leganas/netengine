package by.legan.library.netengine;


import by.legan.library.netengine.controller.ClientController;
import by.legan.library.netengine.controller.ServerController;

/**
 * Created by AndreyLS on 15.02.2017.
 */
public class Assets
{
    public static ServerController serverController;
    public static ClientController clientController;

    public static Users users; // Сюда мы считываем все логины и пароли доступные (хотя можно и при запросе чистать каждый раз)
    public static WorkData workData;
}
