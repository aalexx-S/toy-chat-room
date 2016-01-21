package Client.EventHandler;

import Client.Client;
import Shared.ServerClientMessage;

/**
 * Created by aalexx on 1/15/16.
 */
public class LoginFailedHandler extends Handler {
    public LoginFailedHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg) {
        if (msg.getInstruction() == 110) { // login/register fail
            Client.getLoginPageController().setMessage(msg.getContent());
            return true;
        }
        return false;
    }
}
