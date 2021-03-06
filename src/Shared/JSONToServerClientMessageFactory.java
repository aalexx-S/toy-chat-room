package Shared;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by aalexx on 1/20/16.
 */
public class JSONToServerClientMessageFactory {
    public ServerClientMessage create (JSONObject msg) {
        ServerClientMessageBuilder builder = ServerClientMessageBuilder.create()
                .setInstruction(Integer.parseInt(msg.getString("instruction")));
        if (msg.has("room_id"))
            builder.setRoomId(Integer.parseInt(msg.getString("room_id")));
        if (msg.has("content"))
            builder.setContent(msg.getString("content"));
        if (msg.has("token"))
            builder.setToken(Integer.toString(msg.getInt("token")));
        if (msg.has("list")) {
            List<Map<String, String>> list = new ArrayList<>();
            JSONArray array = msg.getJSONArray("list");
            for (int i = 0 ; i < array.length() ; ++ i) {
                Map<String, String> m = new HashMap<>();
                JSONObject obj = (JSONObject) array.get(i);
                Iterator iter = obj.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    m.put(key, (String) obj.get(key));
                }
                list.add(m);
            }
            builder.setList(list);
        }
        return builder.build();
    }
}
