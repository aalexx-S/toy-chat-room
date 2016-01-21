package Client.EventHandler;

import Client.ClientConnection;
import Shared.ServerClientMessage;

/**
 * Created by aalexx on 1/21/16.
 */
public class AckRequestFileHandler extends Handler {
    public AckRequestFileHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg) {
        if (msg.getInstruction() == 510) { // ack request file
            ClientConnection.getSharedInstance().startRequestFile(msg.getRoomId(), msg.getContent());
            return true;
        }
        return false;
    }
}
