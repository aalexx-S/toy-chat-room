package Client.EventHandler;

import Client.Client;
import Shared.ServerClientMessage;
import javafx.application.Platform;

/**
 * Created by aalexx on 1/21/16.
 */
public class SyncRoomMessageHandler extends Handler {
    public SyncRoomMessageHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg ) {
        if (msg.getInstruction() == 300) { // sync room message
            Platform.runLater(() -> {
                Client.getMainPageController().setUpdateMessage(Integer.toString(msg.getRoomId()), msg.getList());
            });
        }
        return true;
    }
}
