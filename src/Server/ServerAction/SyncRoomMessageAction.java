package Server.ServerAction;

import Server.DatabaseManager.HistoricalMessageManager;
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
                .setInstruction(300).setRoomId(Integer.getInteger(message.get("room_id"))).setList(history).build();
        List<String> receiver = new ArrayList<>();
        receiver.add(message.get("sender_name"));
        //// TODO: 2016/1/20
        //Server.getInstance().send(receiver, responseMessage);
    }
}
