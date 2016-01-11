package Client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/10/16.
 *
 * setMessage(List<Map<String, String>>);
 *  "name", [name]
 *  "type", ["file", other]
 *  "content", [content]   // this will be shown before the download button if type is "file"
 *  "time", [time]         // in second
 *  "id", [file id]        // can be empty if type is not file
 *
 *  add people callback:
 *      pass the person's id
 *
 *  leave:
 *      just an event handler
 *
 *  on download:
 *      pass file id
 *
 *  sendConfirm:
 *      trigger action handler
 */
public class ChatRoom {
    private String type;
    private Stage stage;
    private ChatRoomController controller;
    private String id;

    public ChatRoom (String name, String type) {
        this.type = type;
        this.id = name; // TODO chatroom's id == name ?
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatRoom.fxml"));
            Parent root = loader.load();
            stage = new Stage();
            stage.setTitle(name);
            stage.setScene(new Scene(root, 600, 450));
            controller = loader.getController();
            // hide the add people and leave button at top
            if (type.equals("single")) {
                controller.setHeaderHide(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRoomId () {
        return this.id;
    }

    public void setAddPeopleCallBack (Callback<String, Void> callBack) {
        controller.setAddPeopleAction(callBack);
    }

    public void setLeaveCallBack (Callback<String, Void> callBack) {
        controller.setLeaveCallback(event -> callBack.call(id));
    }

    public void setOnSendConfirm (EventHandler<ActionEvent> callback) {
        controller.setOnSendConfirm(callback);
    }

    public void setOnDownload (Callback<String, Void> callback) {
        controller.setOnDownload(callback);
    }

    public void setMessages (List<Map<String, String>> messages) {
        controller.setMessages(messages);
    }

    public void show() {
        stage.show();
    }

    public void close () {
        stage.close();
    }
}
