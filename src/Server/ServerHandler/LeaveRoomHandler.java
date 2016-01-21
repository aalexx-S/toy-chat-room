package Server.ServerHandler;

import Server.ServerAction.LeaveRoomAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class LeaveRoomHandler extends ServerHandler {
    public LeaveRoomHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("LEAVE_ROOM")) {
            message.put("type", "leave");
            message.put("name", message.get("sender_name"));
            message.put("account", message.get("sender_name"));
            LeaveRoomAction leaveRoomAction = new LeaveRoomAction(message);
            leaveRoomAction.doAction();
            return true;
        }
        else
            return false;
    }
}
