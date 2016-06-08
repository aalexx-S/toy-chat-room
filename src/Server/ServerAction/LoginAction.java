package Server.ServerAction;

import Server.DatabaseManager.AccountManager;
import Server.DatabaseManager.NotifyManager;
import Server.DatabaseManager.OnlineStatusManager;
import Server.DatabaseManager.RoomListManager;
import Server.ServerConnection;
import Server.ServerSecurity;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Tony on 2016/1/20.
 */
public class LoginAction extends ServerAction {

    public LoginAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        AccountManager accountManager = new AccountManager();
        ServerClientMessage responseMessage = null;
        if (accountManager.authenticate(message.get("account"), message.get("password"))) {
            OnlineStatusManager.getInstance().login(message.get("account"));
            RoomListUpdateAction roomListUpdateAction = new RoomListUpdateAction();
            List<Map<String, String>> roomList = roomListUpdateAction.updateUserRoomList(message.get("account"));

            Random rand = new Random();
            boolean repeat = true;
            int token = -1;
            ServerSecurity security = ServerConnection.getInstance().getSecurity();
            while (repeat) {
                token = rand.nextInt();
                repeat = security.checkRepeat(token);
            }
            security.addToken(Integer.valueOf(message.get("sequence_number")), token);

            responseMessage = ServerClientMessageBuilder.create()
                    .setInstruction(100)
                    .setList(roomList)
                    .setToken(Integer.toString(token))
                    .build();
        }
        else {
            responseMessage = ServerClientMessageBuilder.create()
                    .setInstruction(110)
                    .setContent("Login Fail")
                    .build();
        }
        ServerConnection.getInstance().addName(message.get("account"), Integer.valueOf(message.get("sequence_number")));
        ServerConnection.getInstance().send(Integer.valueOf(message.get("sequence_number")), responseMessage);

        //notify user's friends
        NotifyManager notifyManager = new NotifyManager();
        List<String> observers = notifyManager.query(message.get("account"));
        RoomListUpdateAction roomListUpdateAction = new RoomListUpdateAction();
        for (String observer : observers) {
            List<Map<String, String>> roomList = roomListUpdateAction.updateUserRoomList(observer);
            ServerClientMessage forwardMessage = ServerClientMessageBuilder.create()
                    .setInstruction(200)
                    .setList(roomList)
                    .build();
            ServerConnection.getInstance().send(observer, forwardMessage);
        }
    }
}
