package Server.ServerAction;

import Server.DatabaseManager.HistoricalMessageManager;
import Server.ServerConnection;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class SyncRoomMessageAction extends ServerAction {

    public SyncRoomMessageAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        HistoricalMessageManager historicalMessageManager = new HistoricalMessageManager();
        List<Map<String, String>> history = historicalMessageManager.query(message.get("room_id"));
        ServerClientMessage responseMessage = ServerClientMessageBuilder.create()
                .setInstruction(300)
                .setRoomId(Integer.parseInt(message.get("room_id")))
                .setList(history).build();
        ServerConnection.getInstance().send(message.get("sender_name"), responseMessage);
    }
}
