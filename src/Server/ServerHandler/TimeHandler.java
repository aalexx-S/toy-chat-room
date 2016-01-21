package Server.ServerHandler;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class TimeHandler extends ServerHandler {
    public TimeHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        message.put("time_stamp", Long.toString(System.currentTimeMillis() / 1000));
        return false;
    }
}
