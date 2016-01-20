package Client;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by aalexx on 1/10/16.
 */
public class RoomListItem {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty status;
    public RoomListItem (String id, String name, String status) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.status = new SimpleStringProperty(status);
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
    // status
    public void setStatus(String status) {
        this.status.set(status);
    }
    public String getStatus() {
        return status.get();
    }
    public SimpleStringProperty statusProperty() {
        return status;
    }
}
