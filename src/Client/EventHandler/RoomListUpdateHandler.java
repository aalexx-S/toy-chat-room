package Client.EventHandler;

import Client.Client;
import Shared.ServerClientMessage;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/21/16.
 */
public class RoomListUpdateHandler extends Handler {
    public RoomListUpdateHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg) {
        if (msg.getInstruction() == 200) { // room list update
            List<Map<String, String>> sin = new ArrayList<>();
            List<Map<String, String>> mul = new ArrayList<>();
            for (Map<String, String> i : msg.getList()) {
                if (i.get("room_type").equals("single"))
                    sin.add(i);
                else
                    mul.add(i);
            }
            Platform.runLater(() -> {
                Client.getMainPageController().setSingleTableList(sin);
                Client.getMainPageController().setMultipleTableList(mul);
            });
            return true;
        }
        return false;
    }
}
