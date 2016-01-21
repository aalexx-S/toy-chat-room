package Server.ServerHandler;

import Server.ServerAction.LoginAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class LoginHandler extends ServerHandler {
    public LoginHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("LOGIN")) {
            LoginAction loginAction = new LoginAction(message);
            loginAction.doAction();
            return true;
        }
        else
            return false;
    }
}
