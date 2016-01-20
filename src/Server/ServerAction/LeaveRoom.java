package Server.ServerAction;

import Server.DatabaseManager.RoomInfoManager;
import Server.DatabaseManager.RoomListManager;

import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class LeaveRoom extends ServerAction {

    public LeaveRoom(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        message.put("name", message.get("sender_name"));
        RoomInfoManager roomInfoManager = new RoomInfoManager();
        roomInfoManager.update(message);
        RoomListManager roomListManager = new RoomListManager();
        message.put("account", message.get("sender_name"));
        roomListManager.update(message);
    }
}