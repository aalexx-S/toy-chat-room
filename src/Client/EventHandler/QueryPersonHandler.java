package Client.EventHandler;

import Client.Client;
import Shared.ServerClientMessage;
import javafx.application.Platform;

/**
 * Created by aalexx on 1/21/16.
 */
public class QueryPersonHandler extends Handler {
    public QueryPersonHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg) {
        if (msg.getInstruction() == 420) { // query person response
            Platform.runLater(() -> {
                Client.getMainPageController().setQueryPersonMessage(msg.getContent());
            });
            return true;
        }
        return false;
    }
}
