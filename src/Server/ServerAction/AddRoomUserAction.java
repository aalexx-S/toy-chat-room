package Server.ServerAction;

import Server.DatabaseManager.AccountManager;
import Server.DatabaseManager.RoomInfoManager;
import Server.DatabaseManager.RoomListManager;
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
        if (accountManager.query(message.get("name"))) {
            RoomInfoManager roomInfoManager = new RoomInfoManager();
            if (roomInfoManager.check(message.get("room_id"), message.get("name"))) {
                roomInfoManager.update(message);
                response = "SUCCEED";

                RoomListManager roomListManager = new RoomListManager();
                message.put("account", message.get("name"));
                message.put("room_type", "multiple");
                message.put("room_name", roomInfoManager.queryName(message.get("room_id")));
                roomListManager.update(message);
            }
        }

        ServerClientMessage responseMessage = ServerClientMessageBuilder.create()
                .setInstruction(400)
                .setRoomId(Integer.valueOf(message.get("room_id")))
                .setContent(response)
                .build();
        //todo
        //ServerConnection.getInstance().send(message.get("sender_name"), responseMessage);

        RoomListUpdateAction roomListUpdateAction = new RoomListUpdateAction();
        List<Map<String, String>> roomList = roomListUpdateAction.updateUserRoomList(message.get("name"));
        responseMessage = ServerClientMessageBuilder.create()
                        .setInstruction(200)
                        .setList(roomList)
                        .build();
        //todo
        //ServerConnection.getInstance().send(message.get("name"), responseMessage);
    }
}
