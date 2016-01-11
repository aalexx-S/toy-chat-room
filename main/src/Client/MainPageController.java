package Client;

import CustomNode.MyPopupInputButton;
import javafx.fxml.FXML;
import javafx.util.Callback;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/10/16.
 */
public class MainPageController {
    @FXML private RoomList singleTable;
    @FXML private RoomList multipleTable;
    @FXML private MyPopupInputButton find;
    @FXML private MyPopupInputButton build;

    public void setSingleTableList (List<Map<String, String>> roomList) {
        singleTable.setRoomList(roomList);
    }

    public void setMultipleTableList (List<Map<String, String>> roomList) {
        multipleTable.setRoomList(roomList);
    }

    public void setSingleOnEnterAction (Callback<String, Void> callback) {
        singleTable.setOnEnter(callback);
    }

    public void setMultipleOnEnterAction (Callback<String, Void> callback) {
        multipleTable.setOnEnter(callback);
    }

    public void setFindAction (Callback<String, Void> callback) {
        find.setOnConfirm(callback);
    }

    public void setBuildAction (Callback<String, Void> callback) {
        build.setOnConfirm(callback);
    }
}
