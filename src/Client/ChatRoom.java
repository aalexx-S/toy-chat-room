package Client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
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
    private EventHandler<ActionEvent> handler = event -> {};

    public ChatRoom (String name, String type) {
        this.type = type;
        this.id = name;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatRoom.fxml"));
            Parent root = loader.load();
            stage = new Stage();
            stage.setTitle(this.id);
            stage.setScene(new Scene(root, 600, 450));
            controller = loader.getController();
            // hide the add people and leave button at top
            if (type.equals("single")) {
                controller.setHeaderHide(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* on close event */
        stage.setOnCloseRequest(event -> {
            handler.handle(new ActionEvent());
        });
    }

    public File getChosenFile () {
        return controller.getChosenFile();
    }

    public String getInputText () {
        return controller.getInputText();
    }

    public void setOnCloseHandler (EventHandler<ActionEvent> handler) {
        this.handler = handler;
    }

    public void setAddPeopleCallBack (Callback<String, Void> callBack) {
        controller.setAddPeopleCallback(callBack);
    }

    public void setLeaveCallBack (EventHandler<ActionEvent> handler) {
        controller.setLeaveHandler(event -> {
            close();
            handler.handle(new ActionEvent());
        });
    }

    public void setAddPersonMessage (String message){
        controller.setAddPersonMessage(message);
    }

    public void setOnSendConfirm (EventHandler<ActionEvent> callback) {
        controller.setOnSendConfirm(callback);
    }

    public void setHyperlinkCallback(Callback<String, Void> handler) {
        controller.setHyperlinkCallback(handler);
    }

    public void setOnDownload (Callback<String, Void> callback) {
        controller.setOnDownload(callback);
    }

    public void setMessages (List<Map<String, String>> messages) {
        controller.setMessages(messages);
    }

    public void addMessages (Map<String, String> message) {
        controller.addMessages(message);
    }

    public void show() {
        stage.show();
    }

    public void close () {
        stage.close();
    }
}
