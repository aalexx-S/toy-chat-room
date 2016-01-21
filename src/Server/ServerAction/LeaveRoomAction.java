package Server.ServerAction;

import Server.DatabaseManager.RoomInfoManager;
import Server.DatabaseManager.RoomListManager;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class LeaveRoomAction extends ServerAction {

    public LeaveRoomAction(Map<String, String> message) {
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
        RoomListUpdateAction roomListUpdateAction = new RoomListUpdateAction();
        List<Map<String, String>> roomList = roomListUpdateAction.updateUserRoomList(message.get("sender_name"));
        ServerClientMessage responseMessage = ServerClientMessageBuilder.create()
                                            .setInstruction(200)
                                            .setList(roomList)
                                            .build();
        //todo
        //ServerConnection.getInstance().send(message.get("sender_name", responseMessage);
    }
}
