package Client.EventHandler;

import Client.Client;
import Shared.ServerClientMessage;
import javafx.application.Platform;

/**
 * Created by aalexx on 1/21/16.
 */
public class AddRoomMessageHandler extends Handler {
    public AddRoomMessageHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg) {
        if (msg.getInstruction() == 310) { // add room message
            Platform.runLater(() -> {
                Client.getMainPageController().addUpdateMessage(Integer.toString(msg.getRoomId()), msg.getList().get(0));
            });
            return true;
        }
        return false;
    }
}
