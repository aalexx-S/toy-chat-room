package Server.ServerAction;

import Server.DatabaseManager.HistoricalMessageManager;
import Server.DatabaseManager.NotifyManager;
import Server.DatabaseManager.RoomInfoManager;
import Server.DatabaseManager.RoomListManager;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class ReceiveMessageAction extends ServerAction {

    public ReceiveMessageAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        HistoricalMessageManager historicalMessageManger = new HistoricalMessageManager();
        historicalMessageManger.add(message);
        RoomInfoManager roomInfoManager = new RoomInfoManager();
        List<String> receivers = roomInfoManager.query(message.get("room_id"));

        if (roomInfoManager.queryName(message.get("room_id")).equals("")) {
            NotifyManager notifyManager = new NotifyManager();
            List<String> checkList = notifyManager.query(receivers.get(0));
            if (!checkList.contains(receivers.get(1))) {
                notifyManager.update(receivers.get(0), receivers.get(1));
                RoomListManager roomListManager = new RoomListManager();
                message.put("account", receivers.get(1));
                message.put("room_type", "single");
                message.put("room_name", roomInfoManager.queryName(message.get("room_id")));
                roomListManager.update(message);
            }
        }

        List<Map<String, String>> messageInfo = new ArrayList<>();
        message.put("type", "message");
        messageInfo.add(message);
        ServerClientMessage forwardMessage = ServerClientMessageBuilder.create()
                                            .setInstruction(310)
                                            .setRoomId(Integer.valueOf(message.get("room_id")))
                                            .setList(messageInfo)
                                            .build();
        //todo
        //ServerConnection.getInstsance().send(receivers, forwardMessage);
    }
}
