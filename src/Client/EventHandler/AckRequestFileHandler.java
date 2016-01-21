package Client.EventHandler;

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
            // todo
            return true;
        }
        return false;
    }
}
