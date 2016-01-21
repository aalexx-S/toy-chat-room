package Client.EventHandler;

import Client.Client;
import Shared.ServerClientMessage;

/**
 * Created by aalexx on 1/21/16.
 */
public class CreateChatRoomResponseHandler extends Handler {
    public CreateChatRoomResponseHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg) {
        if (msg.getInstruction() == 410) { // create chat room response
            Client.getMainPageController().setBuildRoomMessage(msg.getContent());
            return true;
        }
        return false;
    }
}
