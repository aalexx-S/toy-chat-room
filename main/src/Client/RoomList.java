package Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 1/10/16.
 *
 * setRoolList(List<Map<String, String>>);
 *  id : [id]
 *  name : [name]
 *  status : [ ??? ] not implemented yet
 */
public class RoomList extends VBox implements Initializable {
    @FXML private TableView<RoomListItem> table;
    @FXML private TableColumn name;
    @FXML private TableColumn status;
    @FXML private TableColumn action;
    private Callback<Map<String, String>, Void> callback = param -> null;

    private ObservableList<RoomListItem> roomList = FXCollections.observableArrayList();

    public RoomList () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RoomList.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setRoomList(List<Map<String, String>> roomList) {
        this.roomList = FXCollections.observableArrayList();
        for (Map<String, String> entry : roomList) {
            RoomListItem item = new RoomListItem(entry.get("id"), entry.get("name"), entry.get("status"));
            this.roomList.add(item);
        }
        table.setItems(this.roomList);
    }

    private void onActionCall (String id, String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("id", id);
        callback.call(param);
    }

    public void setOnEnter (Callback<Map<String, String>, Void> callback) {
        this.callback = callback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // init table
        table.setTableMenuButtonVisible(false);
        // init columns
        name.setCellValueFactory(new PropertyValueFactory("name"));
        status.setCellValueFactory(new PropertyValueFactory("status"));
        action.setCellValueFactory(new PropertyValueFactory("status"));
        // init format
        action.setCellFactory(param -> new ActionCell());
    }

    protected class ActionCell extends TableCell<RoomListItem, String> {
        private Button action = new Button("Enter");
        ActionCell () {
            action.setOnAction(event -> {
                int selected = getTableRow().getIndex();
                RoomListItem item = (RoomListItem) table.getItems().get(selected);
                onActionCall(item.getId(), item.getName());
            });
        }

        @Override
        protected void updateItem (String status, boolean empty) {
            if (!empty) {
                setGraphic(action);
            } else
                setGraphic(null);
        }
    }
}
