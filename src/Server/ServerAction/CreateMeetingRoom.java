package Server.ServerAction;

import Server.DatabaseManager.RoomInfoManager;
import Server.DatabaseManager.RoomListManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class CreateMeetingRoom extends ServerAction {

    public CreateMeetingRoom(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        RoomInfoManager roomInfoManager = new RoomInfoManager();
        List<String> roomUsers = new ArrayList<>();
        roomUsers.add(message.get("sender_name"));
        int room_id = roomInfoManager.add(roomUsers, message.get("name"));
        message.put("room_id", Integer.toString(room_id));
        message.put("room_type", "multiple");
        message.put("room_name", message.get("name"));
        message.put("account", message.get("sender_name"));
        RoomListManager roomListManager = new RoomListManager();
        roomListManager.update(message);
        //todo
        //responseMessage
    }
}
