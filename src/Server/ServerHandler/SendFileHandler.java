package Server.ServerHandler;

import Server.ServerAction.ReceiveFileAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class SendFileHandler extends ServerHandler {
    public SendFileHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("SEND_FILE")) {
            ReceiveFileAction receiveFileAction = new ReceiveFileAction(message);
            receiveFileAction.doAction();
            //todo
            return true;
        }
        else
            return false;
    }
}
