package Shared;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/20/16.
 */
public class ServerClientMessage {
    private Integer instruction;
    private Integer RoomId;
    private String token;
    private String content;
    private List<Map<String, String>> list;

    public Integer getInstruction() {
        return instruction;
    }

    public Integer getRoomId() {
        return RoomId;
    }

    public String getToken() {return token;}

    public List<Map<String, String>> getList() {
        return list;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setInstruction(Integer instruction) {
        this.instruction = instruction;
    }

    public void setToken(String token) {this.token = token;}

    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }

    public void setRoomId(Integer roomId) {
        RoomId = roomId;
    }

}
