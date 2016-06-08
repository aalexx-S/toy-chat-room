package Server.ServerHandler;

import Server.ServerConnection;

import java.util.Map;

/**
 * Created by Tony on 2016/6/8.
 */
public class DisconnectHandler extends ServerHandler {
    public DisconnectHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("DISCONNECT")) {
            int sequence = Integer.valueOf(message.get("sequence_number"));
            ServerConnection.getInstance().getSecurity().handleDisconnect(sequence);
            return true;
        }
        else
            return false;
    }
}
