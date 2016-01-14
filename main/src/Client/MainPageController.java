package Client;

import CustomNode.MyPopupInputButton;
import com.sun.deploy.panel.TextFieldProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.util.Callback;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by aalexx on 1/10/16.
 */
public class MainPageController implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    }
}
