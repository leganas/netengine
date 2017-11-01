package by.legan.library.netengine.controller.eventManager;


import by.legan.library.netengine.interfaces.Manager;
import by.legan.library.netengine.interfaces.ProgramController;
import by.legan.library.netengine.network.packeges.serverTOclient.ServerMessage;

/**
 * Занимается обработкой и исполнением
 * всех поступивших в него сообщений ServerMessage
 */
public class ClientEventManager extends Manager<ServerMessage> {

    public ClientEventManager(ProgramController<?> programController) {
        super(programController);
    }
}
