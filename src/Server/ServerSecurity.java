package Server;

import java.util.*;

/**
 * Created by Tony on 2016/6/8.
 */
public class ServerSecurity {
    private Map<Integer, Integer> sequenceTokenMap = new HashMap<>();
    private Map<Integer, Timer> tokenTimerMap = new HashMap<>();

    public boolean checkRepeat(int token) {
        return sequenceTokenMap.containsValue(token);
    }

    public void addToken(int sequence, int token) {
        sequenceTokenMap.put(sequence, token);
    }

    public boolean tokenValid(int sequence, int token) {
        if (sequenceTokenMap.get(sequence) == token)
            return true;
        else if (tokenTimerMap.containsKey(token)) {
            tokenTimerMap.get(token).cancel();
            tokenTimerMap.remove(token);
            sequenceTokenMap.put(sequence, token);
            return true;
        }
        else
            return false;
    }

    public void handleDisconnect(int sequence) {
        int token = sequenceTokenMap.get(sequence);
        sequenceTokenMap.remove(sequence);
        Timer timer = new Timer();
        TokenExpireTask task = new TokenExpireTask();
        task.setToken(token);
        task.setSecurity(this);
        timer.schedule(task, 30000);
    }

    public void tokenExpire(int token) {
        tokenTimerMap.remove(token);
    }
}
