package Client;

import CustomNode.MyPopupInputButton;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by aalexx on 1/10/16.
 */
public class MainPageController implements Initializable {
    @FXML private Text buildRoomMessage;
    @FXML private Text queryPersonMessage;
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
        if (observer.containsKey(id))
            observer.get(id).setMessages(messages);
    }

    public void addUpdateMessage (String id, Map<String, String> message) {
        if (observer.containsKey(id))
            observer.get(id).addMessages(message);
    }

    public void setRoomAddPersonMessage (String id, String message) {
        if (observer.containsKey(id))
            observer.get(id).setAddPersonMessage(message);
    }

    public void setQueryPersonMessage(String message) {
        this.queryPersonMessage.setText(message);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                buildRoomMessage.setText("");
            }
        }, 5000);
    }

    public void setBuildRoomMessage (String message) {
        buildRoomMessage.setText(message);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                buildRoomMessage.setText("");
            }
        }, 5000);
    }

    private void onOpenRoomCall (Map<String, String> param, String type) {
        String roomName = param.get("name");
        String roomId = param.get("id");
        if (!observer.containsKey(roomId)) { // now allowed open if not already opened
            ChatRoom r = new ChatRoom(roomName, type);
            Map<String, String> syncMsg = new HashMap<>();
            syncMsg.put("instruction", "OPEN_ROOM");
            syncMsg.put("room_id", roomId);
            // todo send
            // set send behavior
            r.setOnSendConfirm(event -> {
                String inputText = r.getInputText();
                File chosenFile = r.getChosenFile();
                if (inputText != null && !inputText.equals("")) {
                    Map<String, String> sendTextMsg = new HashMap<String, String>();
                    sendTextMsg.put("instruction", "SEND_MESSAGE");
                    sendTextMsg.put("room_id", roomId);
                    sendTextMsg.put("content", inputText);
                    // todo send
                }
                if (chosenFile != null) {
                    Map<String, String> sendFileMsg = new HashMap<String, String>();
                    sendFileMsg.put("instruction", "SEND_FILE");
                    sendFileMsg.put("room_id", roomId);
                    sendFileMsg.put("content", chosenFile.getName());
                    // todo send message and file
                }
            });
            r.setOnDownload(fileId -> {
                Map<String, String> requestFileMsg = new HashMap<String, String>();
                requestFileMsg.put("instruction", "REQUEST_FILE");
                requestFileMsg.put("name", fileId);
                //todo send
                return null;
            });
            r.setAddPeopleCallBack(name -> {
                Map<String, String> addPersonMsg = new HashMap<String, String>();
                addPersonMsg.put("instruction", "ADD_PERSON");
                addPersonMsg.put("room_id", roomId);
                addPersonMsg.put("name", name);
                // todo send
                return null;
            });
            r.setLeaveCallBack(event -> {
                Map<String, String> leaveMsg = new HashMap<String, String>();
                leaveMsg.put("instruction", "LEAVE_ROOM");
                leaveMsg.put("room_id", roomId);
                // todo send
            });
            // attach
            observer.put(roomId, r);
            // detach on close
            r.setOnCloseHandler(event -> observer.remove(roomId));
            r.show();
        }
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
        find.setOnConfirm(param -> {
            Map<String, String> queryNameMsg = new HashMap<String, String>();
            queryNameMsg.put("instruction", "CREATE_CHAT_ROOM");
            queryNameMsg.put("name", param);
            // todo send
            return null;
        });
        build.setTextFieldChangeListener((observable, oldValue, newValue) -> {
            if (!FIND_FILTER.matcher((String)newValue).matches()) {
                ((Property)observable).setValue(oldValue);
            }
        });
        build.setOnConfirm(param -> {
            Map<String, String> buildRoomMsg = new HashMap<String, String>();
            buildRoomMsg.put("instruction", "CREATE_MEETING_ROOM");
            buildRoomMsg.put("room_name", param);
            // todo send
            return null;
        });
        // hide status on multiple
        multipleTable.setStatusVisible(false);
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
}
