package Server.ServerHandler;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class LogoutHandler extends ServerHandler {
    public LogoutHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        //todo
        return true;
    }
}
