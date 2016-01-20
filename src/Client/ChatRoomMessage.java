package Client;

import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aalexx on 1/11/16.
 */
public class ChatRoomMessage {
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleStringProperty content;
    private SimpleStringProperty time;
    private SimpleStringProperty id;
    public ChatRoomMessage (String name, String type, String content, String time, String id) {
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.content = new SimpleStringProperty(content);
        Date date = new Date(Long.parseLong(time) * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM HH:mm:ss");
        this.time = new SimpleStringProperty(sdf.format(date));
        this.id = new SimpleStringProperty(id);
    }
    // name
    public void setName(String name) {
        this.name.set(name);
    }
    public String getName() {
        return name.get();
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    // type
    public void setType(String type) {
        this.type.set(type);
    }
    public String getType() {
        return type.get();
    }
    public SimpleStringProperty typeProperty() {
        return type;
    }
    // content
    public void setContent(String content) {
        this.content.set(content);
    }
    public String getContent() {
        return content.get();
    }
    public SimpleStringProperty contentProperty() {
        return content;
    }
    // time
    public void setTime(String time) {
        this.time.set(time);
    }
    public String getTime() {
        return time.get();
    }
    public SimpleStringProperty timeProperty() {
        return time;
    }
    // id
    public void setId(String id) {
        this.id.set(id);
    }
    public String getId() {
        return id.get();
    }
    public SimpleStringProperty idProperty() {
        return id;
    }
}
