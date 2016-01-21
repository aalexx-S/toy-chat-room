package Shared;

import org.json.JSONObject;

/**
 * Created by aalexx on 1/20/16.
 */
public class ServerClientMessageToJSONFactory {
    public JSONObject create (ServerClientMessage msg) {
        JSONObject ret = new JSONObject();
        try {
            ret.put("instruction", msg.getInstruction());
            if (msg.getContent() != null)
                ret.put("content", msg.getContent());
            if (msg.getRoomId() != null)
                ret.put("room_id", msg.getRoomId());
            if (msg.getList() != null)
                ret.put("list", msg.getList());
        } catch (Exception e) {
        }
        return ret;
    }
}
