package Shared;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/20/16.
 */
public class ServerClientMessageBuilder {
    ServerClientMessage ret = new ServerClientMessage();
    private ServerClientMessageBuilder () {
        ret = new ServerClientMessage();
    }

    public static ServerClientMessageBuilder create () {
        return new ServerClientMessageBuilder();
    }

    public ServerClientMessageBuilder setInstruction (Integer instruction) {
        ret.setInstruction(instruction);
        return this;
    }

    public ServerClientMessageBuilder setRoomId (Integer roomId) {
        ret.setRoomId(roomId);
        return this;
    }

    public ServerClientMessageBuilder setToken (Integer token) {
        ret.setToken(token);
        return this;
    }

    public ServerClientMessageBuilder setContent (String content) {
        ret.setContent(content);
        return this;
    }

    public ServerClientMessageBuilder setList (List<Map<String, String>> list) {
        ret.setList(list);
        return this;
    }

    public ServerClientMessage build () {
        return ret;
    }
}
