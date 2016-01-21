package Client.EventHandler;

import Shared.ServerClientMessage;

/**
 * Created by aalexx on 1/15/16.
 *
 * doAction (Object target): boolean
 *  return true to consume the event
 *  return false to pass to next handler
 *
 */
public class Handler {
    private Handler next;

    public Handler (Handler next) {
        this.next = next;
    }

    protected boolean doAction (ServerClientMessage target) {
        return true;
    };

    public void handle (ServerClientMessage target) {
        boolean consumed = doAction(target);
        if (!consumed)
            nextHandler(target);
    };

    protected void nextHandler (ServerClientMessage target) {
        next.handle(target);
    }
}
