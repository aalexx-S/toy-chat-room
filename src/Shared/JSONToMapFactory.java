package Shared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Tony on 2016/1/20.
 */
public class JSONToMapFactory {
    public Map<String, String> create(JSONObject message, String senderName, String sequence) {
        Map<String, String> ret = new HashMap<>();
        if (senderName == null) {
            ret.put("sequence_number", sequence);
        } else {
            ret.put("sender_name", senderName);
        }
        Iterator iter = message.keys();
        while (iter.hasNext()) {
            String k = (String) iter.next();
            try {
                ret.put(k, message.getString(k));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
