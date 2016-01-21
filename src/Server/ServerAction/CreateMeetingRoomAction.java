package Server.ServerAction;

import Server.DatabaseManager.RoomInfoManager;
import Server.DatabaseManager.RoomListManager;
import Server.ServerConnection;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class CreateMeetingRoomAction extends ServerAction {

    public CreateMeetingRoomAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        RoomInfoManager roomInfoManager = new RoomInfoManager();
        List<String> roomUsers = new ArrayList<>();
        roomUsers.add(message.get("sender_name"));
        int room_id = roomInfoManager.add(roomUsers, message.get("name"));
        message.put("room_id", Integer.toString(room_id));
        RoomListManager roomListManager = new RoomListManager();
        roomListManager.update(message);

        RoomListUpdateAction roomListUpdateAction = new RoomListUpdateAction();
        List<Map<String, String>> roomList = roomListUpdateAction.updateUserRoomList(message.get("sender_name"));
        ServerClientMessage responseMessage = ServerClientMessageBuilder.create()
                                            .setInstruction(200)
                                            .setList(roomList)
                                            .build();
        ServerConnection.getInstance().send(message.get("sender_name"), responseMessage);
    }
}
