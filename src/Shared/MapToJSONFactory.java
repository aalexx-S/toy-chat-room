package Shared;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by aalexx on 1/21/16.
 */
public class MapToJSONFactory {
    public JSONObject create (Map<String, String> message) {
        JSONObject ret = new JSONObject();
        for (Map.Entry<String, String> i : message.entrySet()) {
            ret.put(i.getKey(), i.getValue());
        }
        return ret;
    }
}
