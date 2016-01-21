package Client.EventHandler;

import Client.Client;
import Shared.ServerClientMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/21/16.
 */
public class LoginSuccessHandler extends Handler {
    private Client client;

    public LoginSuccessHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg) {
        if (msg.getInstruction() == 100) { // login success
            client.loginSuccess();
            List<Map<String, String>> sin = new ArrayList<>();
            List<Map<String, String>> mul = new ArrayList<>();
            for (Map<String, String> i : msg.getList()) {
                if (i.get("room_type").equals("single"))
                    sin.add(i);
                else
                    mul.add(i);
            }
            Client.getMainPageController().setSingleTableList(sin);
            Client.getMainPageController().setMultipleTableList(mul);
            return true;
        }
        return false;
    }

    public void setClient (Client client) {
        this.client = client;
    }
}