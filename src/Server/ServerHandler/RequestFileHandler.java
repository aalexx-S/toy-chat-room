package Server.ServerHandler;

import Server.ServerAction.SendFileAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class RequestFileHandler extends ServerHandler {
    public RequestFileHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("REQUEST_FILE")) {
            SendFileAction sendFileAction = new SendFileAction(message);
            sendFileAction.doAction();
            return true;
        }
        else
            return false;
    }
}
