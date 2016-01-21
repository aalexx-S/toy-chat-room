package Server.ServerHandler;

import Server.ServerAction.LogoutAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class LogoutHandler extends ServerHandler {
    public LogoutHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("LOGOUT")) {
            LogoutAction logoutAction = new LogoutAction(message);
            logoutAction.doAction();
            return true;
        }
        else
            return false;
    }
}
