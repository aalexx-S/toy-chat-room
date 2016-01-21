package Server.ServerHandler;

import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class ServerHandler {
    private ServerHandler nextHandler;

    public ServerHandler(ServerHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public boolean doAction(Map<String, String> message) {
        return true;
    }

    public void handle(Map<String, String> message) {
        boolean consumed = doAction(message);
        if (!consumed)
            doNext(message);
    }

    public void doNext(Map<String, String> message) {
        if (nextHandler != null)
            nextHandler.handle(message);
    }
}
