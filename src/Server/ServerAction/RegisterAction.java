package Server.ServerAction;

import Server.DatabaseManager.AccountManager;
import Server.DatabaseManager.NotifyManager;
import Server.DatabaseManager.RoomListManager;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class RegisterAction extends ServerAction {

    public RegisterAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        AccountManager accountManager = new AccountManager();
        ServerClientMessage responseMessage = null;
        if (accountManager.query(message.get("account"))) {
            responseMessage = ServerClientMessageBuilder.create()
                    .setInstruction(110)
                    .setContent("Register Fail")
                    .build();
            //// TODO: 2016/1/20
            //send responseMessage back
        }
        else {
            accountManager.add(message);
            RoomListManager roomListManager = new RoomListManager();
            roomListManager.add(message.get("account"));
            NotifyManager notifyManager = new NotifyManager();
            notifyManager.add(message.get("account"));
            LoginAction loginAction = new LoginAction(message);
            loginAction.doAction();
        }
    }
}
