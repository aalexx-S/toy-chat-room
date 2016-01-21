package Server.ServerHandler;

import Server.ServerAction.AddRoomUserAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class AddPersonHandler extends ServerHandler {
    public AddPersonHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("ADD_PERSON")) {
            AddRoomUserAction addRoomUserAction = new AddRoomUserAction(message);
            addRoomUserAction.doAction();
            return true;
        }
        else
            return false;
    }
}
