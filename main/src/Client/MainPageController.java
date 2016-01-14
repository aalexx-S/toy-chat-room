package Client;

import CustomNode.MyPopupInputButton;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by aalexx on 1/10/16.
 */
public class MainPageController implements Initializable {
    @FXML private RoomList singleTable;
    @FXML private RoomList multipleTable;
    @FXML private MyPopupInputButton find;
    @FXML private MyPopupInputButton build;
    private  Map<String, ChatRoom> observer = new HashMap<>(); // room id -> instance

    public void setSingleTableList (List<Map<String, String>> roomList) {
        singleTable.setRoomList(roomList);
    }

    public void setMultipleTableList (List<Map<String, String>> roomList) {
        multipleTable.setRoomList(roomList);
    }

    public void setSingleOnEnterAction (Callback<Map<String, String>, Void> callback) {
        singleTable.setOnEnter(callback);
    }

    public void setMultipleOnEnterAction (Callback<Map<String, String>, Void> callback) {
        multipleTable.setOnEnter(callback);
    }

    public void setFindAction (Callback<String, Void> callback) {
        find.setOnConfirm(callback);
    }

    public void setBuildAction (Callback<String, Void> callback) {
        build.setOnConfirm(callback);
    }

    public void setUpdateMessage (String id, List<Map<String, String>> messages) {
        observer.get(id).setMessages(messages);
    }

    public void addUpdateMessage (String id, Map<String, String> message) {
        observer.get(id).addMessages(message);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // input filter
        Pattern FIND_FILTER = Pattern.compile("^[^\\.]*$");
        find.setTextFieldChangeListener((observable, oldValue, newValue) -> {
            if (!FIND_FILTER.matcher((String)newValue).matches()) {
                ((Property)observable).setValue(oldValue);
            }
        });
        build.setTextFieldChangeListener((observable, oldValue, newValue) -> {
            if (!FIND_FILTER.matcher((String)newValue).matches()) {
                ((Property)observable).setValue(oldValue);
            }
        });
        // open chat room
        setSingleOnEnterAction(param -> {
            onOpenRoomCall(param, "single");
            return null;
        });
        setMultipleOnEnterAction(param -> {
            onOpenRoomCall(param, "multiple");
            return null;
        });
    }

    private void onOpenRoomCall (Map<String, String> param, String type) {
        String roomName = param.get("name");
        String roomId = param.get("id");
        if (!observer.containsKey(roomId)) { // now allowed open if not already opened
            ChatRoom r = new ChatRoom(roomName, type);
            // set send behavior
            r.setOnSendConfirm(event -> {
                String inputText = r.getInputText();
                File chosenFile = r.getChosenFile();
                if (inputText != null && !inputText.equals("")) {
                    // send text

                }
                if (chosenFile != null) {
                    // send file

                }
            });
            // set download behavior
            r.setOnDownload(fileId -> {
                return null;
            });
            // attach
            observer.put(roomId, r);
            // detach on close
            r.setOnCloseHandler(event -> observer.remove(roomId));
            r.show();
        }
    }
}
