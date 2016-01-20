package Server.ServerAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public abstract class ServerAction {
    protected Map<String, String> message;

    public ServerAction(Map<String, String> message) {
        this.message = message;
    }

    public abstract void doAction();
}
