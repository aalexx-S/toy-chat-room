package Client.EventHandler;

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

    public boolean doAction (Object target) {
        return true;
    };

    public void handle (Object target) {
        boolean consumed = doAction(target);
        if (!consumed)
            nextHandler(target);
    };

    public void nextHandler (Object target) {
        next.handle(target);
    }
}
