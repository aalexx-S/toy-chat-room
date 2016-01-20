package Server.ServerAction;

import Server.DatabaseManager.HistoricalMessageManager;
import Server.DatabaseManager.RoomInfoManager;
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

        List<Map<String, String>> messageInfo = new ArrayList<>();
        message.put("type", "message");
        messageInfo.add(message);
        ServerClientMessage forwardMessage = ServerClientMessageBuilder.create()
                                            .setInstruction(310)
                                            .setRoomId(Integer.valueOf(message.get("room_id")))
                                            .setList(messageInfo)
                                            .build();
        //todo
        //ServerConnection.getInstsance().send(forwardMessage, receivers);
    }
}
