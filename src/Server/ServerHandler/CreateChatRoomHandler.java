package Server.ServerHandler;

import Server.ServerAction.CreateChatRoomAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class CreateChatRoomHandler extends ServerHandler {
    public CreateChatRoomHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("CREATE_CHAT_ROOM")) {
            CreateChatRoomAction createChatRoomAction = new CreateChatRoomAction(message);
            createChatRoomAction.doAction();
            return true;
        }
        else
            return false;
    }
}