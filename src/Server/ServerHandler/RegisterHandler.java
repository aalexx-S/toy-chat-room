package Server.ServerHandler;

import Server.ServerAction.RegisterAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class RegisterHandler extends ServerHandler {
    public RegisterHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("REGISTER")) {
            RegisterAction registerAction = new RegisterAction(message);
            registerAction.doAction();
            return true;
        }
        else
            return false;
    }
}
