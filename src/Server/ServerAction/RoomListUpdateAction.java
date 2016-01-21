package Server.ServerAction;

import Server.DatabaseManager.OnlineStatusManager;
import Server.DatabaseManager.RoomInfoManager;
import Server.DatabaseManager.RoomListManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class RoomListUpdateAction {
    public List<Map<String, String>> updateUserRoomList(String account) {
        RoomListManager roomListManager = new RoomListManager();
        List<Map<String, String>> roomList = roomListManager.query(account);
        List<String> roomIds = new ArrayList<>();
        for (Map<String, String> room : roomList) {
            roomIds.add(room.get("room_id"));
        }
        RoomInfoManager roomInfoManager = new RoomInfoManager();
        List<String> friends = roomInfoManager.queryFriends(roomIds, account);
        List<String> status = OnlineStatusManager.getInstance().query(friends);
        for (int i = 0; i < status.size(); i++)
            roomList.get(i).put("status", status.get(i));
        return roomList;
    }
}
