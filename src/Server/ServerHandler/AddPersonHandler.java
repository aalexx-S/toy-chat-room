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
            message.put("type", "add");
            message.put("account", message.get("name"));
            message.put("room_type", "multiple");
            AddRoomUserAction addRoomUserAction = new AddRoomUserAction(message);
            addRoomUserAction.doAction();
            return true;
        }
        else
            return false;
    }
}
