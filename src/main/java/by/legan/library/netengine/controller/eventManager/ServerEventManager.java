package by.legan.library.netengine.controller.eventManager;

import by.legan.library.netengine.interfaces.Manager;
import by.legan.library.netengine.interfaces.ProgramController;
import by.legan.library.netengine.network.packeges.clientTOserver.ClientMessage;

/**
 * Занимается обработкой и исполнением
 * всех поступивших в него сообщений типа ClientMessage
 */
public class ServerEventManager extends Manager<ClientMessage> {

    public ServerEventManager(ProgramController<?> programController) {
        super(programController);
    }
}
