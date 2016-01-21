package Server.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony on 2016/1/21.
 */
public class OnlineStatusManager {
    private List<String> onlineUsers;
    private static OnlineStatusManager sharedInstance;

    private OnlineStatusManager() {
        onlineUsers = new ArrayList<>();
    }

    public static OnlineStatusManager getInstance() {
        if (sharedInstance == null) {
            synchronized (OnlineStatusManager.class) {
                sharedInstance = new OnlineStatusManager();
            }
        }
        return sharedInstance;
    }

    public void login(String account) {
        onlineUsers.add(account);
    }

    public void logout(String account) {
        onlineUsers.remove(account);
    }

    public List<String> query(List<String> friends) {
        List<String> response = new ArrayList<>();
        for (String friend : friends) {
            if (friend.equals(""))
                response.add("");
            else if (onlineUsers.contains(friend))
                response.add("online");
            else
                response.add("offline");
        }
        return response;
    }
}
