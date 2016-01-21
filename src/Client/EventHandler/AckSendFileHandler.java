package Client.EventHandler;

import Shared.ServerClientMessage;

/**
 * Created by aalexx on 1/21/16.
 */
public class AckSendFileHandler extends Handler {
    public AckSendFileHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg) {
        if (msg.getInstruction() == 500) { // ack send file
            // todo
        }
        return false;
    }
}
