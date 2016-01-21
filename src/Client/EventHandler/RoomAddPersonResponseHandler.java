package Client.EventHandler;

import Client.Client;
import Shared.ServerClientMessage;
import javafx.application.Platform;

/**
 * Created by aalexx on 1/21/16.
 */
public class RoomAddPersonResponseHandler extends Handler {
    public RoomAddPersonResponseHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg) {
        if (msg.getInstruction() == 400) { // add person response
            Platform.runLater(() -> {
                Client.getMainPageController().setRoomAddPersonMessage(Integer.toString(msg.getRoomId()), msg.getContent());
            });
            return true;
        }
        return false;
    }
}
