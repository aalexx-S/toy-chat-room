package Server.ServerAction;

import Server.DatabaseManager.AccountManager;
import Server.DatabaseManager.RoomListManager;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            RoomListManager roomListManager = new RoomListManager();
            List<Map<String, String>> roomList = roomListManager.query(message.get("account"));
            //todo
            //online status init
            //a static table for online status
            responseMessage = ServerClientMessageBuilder.create()
                    .setInstruction(100)
                    .setList(roomList)
                    .build();
        }
        else {
            responseMessage = ServerClientMessageBuilder.create()
                    .setInstruction(110)
                    .setContent("Login Fail")
                    .build();
        }
        //send back responseMessage
    }
}
