package Client.EventHandler;

import Client.Client;
import Client.ClientConnection;
import Shared.ServerClientMessage;
import javafx.application.Platform;

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
            ClientConnection.getSharedInstance().setToken(msg.getToken());
            List<Map<String, String>> sin = new ArrayList<>();
            List<Map<String, String>> mul = new ArrayList<>();
            for (Map<String, String> i : msg.getList()) {
                if (i.get("room_type").equals("single"))
                    sin.add(i);
                else
                    mul.add(i);
            }
            Platform.runLater(() -> {
                Client.getMainPageController().setSingleTableList(sin);
                Client.getMainPageController().setMultipleTableList(mul);
                client.loginSuccess();
            });
            return true;
        }
        return false;
    }

    public void setClient (Client client) {
        this.client = client;
    }
}
