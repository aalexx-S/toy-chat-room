package Server.ServerHandler;

import Server.ServerAction.SyncRoomMessageAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class OpenRoomHandler extends ServerHandler {
    public OpenRoomHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("OPEN_ROOM")) {
            SyncRoomMessageAction syncRoomMessageAction = new SyncRoomMessageAction(message);
            syncRoomMessageAction.doAction();
            return true;
        }
        else
            return false;
    }
}
