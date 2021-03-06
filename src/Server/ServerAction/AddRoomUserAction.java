package Server.ServerAction;

import Server.DatabaseManager.AccountManager;
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
public class AddRoomUserAction extends ServerAction {

    public AddRoomUserAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        String response = "FAIL";
        AccountManager accountManager = new AccountManager();
        ServerClientMessage responseMessage = null;
        if (accountManager.query(message.get("name"))) {
            RoomInfoManager roomInfoManager = new RoomInfoManager();
            if (roomInfoManager.check(message.get("room_id"), message.get("name"))) {
                roomInfoManager.update(message);
                response = "SUCCEED";

                RoomListManager roomListManager = new RoomListManager();
                message.put("room_name", roomInfoManager.queryName(message.get("room_id")));
                roomListManager.update(message);
            }
        }
        else {
            responseMessage = ServerClientMessageBuilder.create()
                    .setInstruction(400)
                    .setRoomId(Integer.valueOf(message.get("room_id")))
                    .setContent(response)
                    .build();
            ServerConnection.getInstance().send(message.get("sender_name"), responseMessage);
            return;
        }

        RoomListUpdateAction roomListUpdateAction = new RoomListUpdateAction();
        List<Map<String, String>> roomList = roomListUpdateAction.updateUserRoomList(message.get("name"));
        responseMessage = ServerClientMessageBuilder.create()
                        .setInstruction(200)
                        .setList(roomList)
                        .build();
        ServerConnection.getInstance().send(message.get("name"), responseMessage);
    }
}
