package Server.ServerAction;

import Server.DatabaseManager.NotifyManager;
import Server.DatabaseManager.OnlineStatusManager;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class LogoutAction extends ServerAction {

    public LogoutAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        OnlineStatusManager.getInstance().logout(message.get("sender_name"));
        NotifyManager notifyManager = new NotifyManager();
        List<String> observers = notifyManager.query(message.get("sender_name"));
        RoomListUpdateAction roomListUpdateAction = new RoomListUpdateAction();
        for (String observer : observers) {
            List<Map<String, String>> roomList = roomListUpdateAction.updateUserRoomList(observer);
            ServerClientMessage forwardMessage = ServerClientMessageBuilder.create()
                                                .setInstruction(200)
                                                .setList(roomList)
                                                .build();
            //ServerConnection.getInstance().send(observer, forwardMessage);
        }
        //detach account from connection map
    }
}
