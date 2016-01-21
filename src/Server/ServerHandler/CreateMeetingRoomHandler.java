package Server.ServerHandler;

import Server.ServerAction.CreateMeetingRoomAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class CreateMeetingRoomHandler extends ServerHandler {
    public CreateMeetingRoomHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("CREATE_MEETING_ROOM")) {
            message.put("room_type", "multiple");
            message.put("account", message.get("sender_name"));
            message.put("type", "add");
            CreateMeetingRoomAction createMeetingRoomAction = new CreateMeetingRoomAction(message);
            createMeetingRoomAction.doAction();
            return true;
        }
        else
            return false;
    }
}
