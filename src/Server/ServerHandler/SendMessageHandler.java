package Server.ServerHandler;

import Server.ServerAction.ReceiveMessageAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class SendMessageHandler extends ServerHandler {
    public SendMessageHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("SEND_MESSAGE")) {
            ReceiveMessageAction receiveMessageAction = new ReceiveMessageAction(message);
            receiveMessageAction.doAction();
            return true;
        }
        else
            return false;
    }
}
