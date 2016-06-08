package Server;

import java.util.TimerTask;

/**
 * Created by Tony on 2016/6/8.
 */
public class TokenExpireTask extends TimerTask{
    private int token;
    private ServerSecurity security;

    public void run() {
        security.tokenExpire(token);
        System.err.println("Invalidate token " + String.valueOf(token));
    }

    public void setToken(int token) {
        this.token = token;
    }

    public void setSecurity(ServerSecurity security) {
        this.security = security;
    }
}
