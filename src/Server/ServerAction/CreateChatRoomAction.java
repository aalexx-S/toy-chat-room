package Server.ServerAction;

import Server.DatabaseManager.AccountManager;
import Server.DatabaseManager.NotifyManager;
import Server.DatabaseManager.RoomInfoManager;
import Server.DatabaseManager.RoomListManager;
import Server.ServerConnection;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class CreateChatRoomAction extends ServerAction {

    public CreateChatRoomAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        AccountManager accountManager = new AccountManager();
        ServerClientMessage responseMessage = null;
        if (accountManager.query(message.get("name"))) {
            //check if object has already added subject
            NotifyManager notifyManager = new NotifyManager();
            if (notifyManager.query(message.get("name")).contains(message.get("sender_name")))
                return;
            /*else if (notifyManager.query(message.get("sender_name")).contains(message.get("name"))) {
                RoomListManager roomListManager = new RoomListManager();
                String existedId =
                        roomListManager.getMutualRoomID(message.get("name"), message.get("sender_name"));
                Map<String, String> existedRoom = new HashMap<>();
                existedRoom.put("account", message.get("sender_name"));
                existedRoom.put("type", "add");
                existedRoom.put("room_id", existedId);
                existedRoom.put("room_type", "single");
                existedRoom.put("room_name", message.get("name"));
                roomListManager.update(existedRoom);
            }*/
            else {
                RoomInfoManager roomInfoManager = new RoomInfoManager();
                List<String> roomUsers = new ArrayList<>();
                roomUsers.add(message.get("sender_name"));
                roomUsers.add(message.get("name"));
                int room_id = roomInfoManager.add(roomUsers, "");
                message.put("room_id", Integer.toString(room_id));
                RoomListManager roomListManager = new RoomListManager();
                roomListManager.update(message);
            }

            notifyManager.update(message.get("name"), message.get("sender_name"));
            notifyManager.update(message.get("sender_name"), message.get("name"));
            responseMessage = ServerClientMessageBuilder.create()
                    .setInstruction(420)
                    .setContent("The user exists!")
                    .build();
        }
        else {
            responseMessage = ServerClientMessageBuilder.create()
                    .setInstruction(420)
                    .setContent("No Such User")
                    .build();
        }
        ServerConnection.getInstance().send(message.get("sender_name"), responseMessage);

        RoomListUpdateAction roomListUpdateAction = new RoomListUpdateAction();
        List<Map<String, String>> roomList = roomListUpdateAction.updateUserRoomList(message.get("sender_name"));
        responseMessage = ServerClientMessageBuilder.create()
                        .setInstruction(200)
                        .setList(roomList)
                        .build();
        ServerConnection.getInstance().send(message.get("sender_name"), responseMessage);
        ServerConnection.getInstance().send(message.get("name"), responseMessage);
    }
}
