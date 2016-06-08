package Server.ServerAction;

import Server.DatabaseManager.*;
import Server.ServerConnection;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class UploadFinishAction extends ServerAction {
    public UploadFinishAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        FileManager fileManager = new FileManager();
        Map<String, String> fileMessage = new HashMap<>();
        System.err.println("file id: " + message.get("file_id"));
        fileMessage.put("room_id", fileManager.queryRoomID(message.get("file_id")));
        fileMessage.put("sender_name", fileManager.querySenderName(message.get("file_id")));
        fileMessage.put("content", fileManager.queryFileName(message.get("file_id")));
        fileMessage.put("time_stamp", message.get("time_stamp"));
        fileMessage.put("type", message.get("type"));
        fileMessage.put("file_id", message.get("file_id"));
        fileMessage.put("token", message.get("token"));
        System.err.println(fileMessage);

        HistoricalMessageManager historicalMessageManger = new HistoricalMessageManager();
        historicalMessageManger.add(fileMessage);
        RoomInfoManager roomInfoManager = new RoomInfoManager();
        List<String> receivers = roomInfoManager.query(fileMessage.get("room_id"));

        /*if (roomInfoManager.queryName(fileMessage.get("room_id")).equals("")) {
            NotifyManager notifyManager = new NotifyManager();
            List<String> checkList = notifyManager.query(receivers.get(0));
            if (!checkList.contains(receivers.get(1))) {
                notifyManager.update(receivers.get(0), receivers.get(1));
                RoomListManager roomListManager = new RoomListManager();
                Map<String, String> roomMessage = new HashMap<>();
                roomMessage.put("account", receivers.get(1));
                roomMessage.put("room_id", fileMessage.get("room_id"));
                roomMessage.put("room_type", "single");
                roomMessage.put("room_name", receivers.get(0));
                roomMessage.put("type", "add");
                roomListManager.update(roomMessage);

                RoomListUpdateAction roomListUpdateAction = new RoomListUpdateAction();
                List<Map<String, String>> roomList = roomListUpdateAction.updateUserRoomList(receivers.get(1));
                ServerClientMessage forwardMessage = ServerClientMessageBuilder.create()
                        .setInstruction(200)
                        .setList(roomList)
                        .build();
                ServerConnection.getInstance().send(receivers.get(1), forwardMessage);
            }
        }*/

        List<Map<String, String>> messageInfo = new ArrayList<>();
        messageInfo.add(fileMessage);
        ServerClientMessage forwardMessage = ServerClientMessageBuilder.create()
                .setInstruction(310)
                .setRoomId(Integer.valueOf(fileMessage.get("room_id")))
                .setList(messageInfo)
                .build();
        for (String receiver : receivers)
            ServerConnection.getInstance().send(receiver, forwardMessage);
    }
}
