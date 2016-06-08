package Client;

import CustomNode.MyConfirmationButton;
import CustomNode.MyFileChooser;
import CustomNode.MyPopupInputButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by aalexx on 1/10/16.
 *
 * setMessage(List<Map<String, String>>);
 *  "sender_name", [name]
 *  "type", ["file", other]
 *  "content", [content]   // this will be shown before the download button if type is "file"
 *  "time", [time]         // in second
 *  "file_id", [file id]        // can be empty if type is not file
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
 *      trigger action handler;
 */
public class ChatRoomController implements Initializable {
    @FXML private Text addPersonMessage;
    @FXML private MyConfirmationButton confirm;
    @FXML private MyFileChooser fileChooser;
    @FXML private TextField inputTextField;
    @FXML private VBox header;
    @FXML private MyPopupInputButton add;
    @FXML private MyConfirmationButton leave;
    @FXML private TableView<ChatRoomMessage> contentTable;
    @FXML private TableColumn time;
    @FXML private TableColumn name;
    @FXML private TableColumn content;
    private ObservableList<ChatRoomMessage> messages = FXCollections.observableArrayList();
    private EventHandler<ActionEvent> confirmHandler = param -> {};
    private Callback<String, Void> downloadCallback = param -> null;

    public void setAddPeopleCallback (Callback<String, Void> callback) {
        add.setOnConfirm(callback);
    }

    public void setLeaveHandler(EventHandler<ActionEvent> handler) {
        leave.setOkAction(handler);
    }

    public String getInputText () {
        return inputTextField.getText();
    }

    public File getChosenFile () {
        return fileChooser.getChosenFile();
    }

    public void setOnSendConfirm (EventHandler<ActionEvent> callback) {
        this.confirmHandler = callback;
    }

    public void setOnDownload (Callback<String, Void> callback) {
        this.downloadCallback = callback;
    }

    public void setMessages (List<Map<String, String>> messages) {
        this.messages = FXCollections.observableArrayList();
        for (Map<String, String> entry : messages) {
            ChatRoomMessage item = new ChatRoomMessage(entry.get("sender_name"), entry.get("type"), entry.get("content"),
                    entry.get("time_stamp"), entry.get("file_id"));
            this.messages.add(item);
        }
        contentTable.setItems(this.messages);
    }

    public void addMessages (Map<String, String> entry) {
        ChatRoomMessage item = new ChatRoomMessage(entry.get("sender_name"), entry.get("type"), entry.get("content"),
                entry.get("time_stamp"), entry.get("file_id"));
        this.messages.add(item);
        contentTable.setItems(this.messages);
    }

    public void setAddPersonMessage (String message) {
        addPersonMessage.setText(message);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                addPersonMessage.setText("");
            }
        }, 5000);
    }

    public void onActionCall (String downloadId) {
        downloadCallback.call(downloadId);
    }

    public void setHeaderHide(boolean value) {
        header.setVisible(value);
        header.setManaged(value);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // init table
        contentTable.setTableMenuButtonVisible(false);
        // init table columns
        time.setCellValueFactory(new PropertyValueFactory("time"));
        name.setCellValueFactory(new PropertyValueFactory("name"));
        content.setCellValueFactory(new PropertyValueFactory("content"));
        // format
        content.setCellFactory(param -> new ContentCell());
        // init button input/upload stuff
        confirm.setDisable(true);
        confirm.setOkAction(event -> {
            confirmHandler.handle(new ActionEvent());
            confirm.setDisable(true);
            inputTextField.clear();
            inputTextField.requestFocus();
            fileChooser.clear();
        });
        inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.equals("")) {
                confirm.setDisable(true);
            } else
                confirm.setDisable(false);
        });
        fileChooser.addListener((observable, oldValue, newValue) -> {
            if (newValue == null)
                confirm.setDisable(true);
            else
                confirm.setDisable(false);
        });
        // focus textField
        inputTextField.requestFocus();
    }

    protected class ContentCell extends TableCell<RoomListItem, String> {
        private Button action = new Button("Download");
        ContentCell() {
            action.setOnAction(event -> {
                int selected = getTableRow().getIndex();
                ChatRoomMessage item = contentTable.getItems().get(selected);
                onActionCall(item.getId());
            });
        }

        @Override
        protected void updateItem (String content, boolean empty) {
            Text contentText = new Text(content);
            if (!empty) {
                String type = contentTable.getItems().get(getIndex()).getType();
                if (type.equals("file")) {
                    /*
                        Show file download button.
                    */
                    HBox tmp = new HBox();
                    tmp.setSpacing(20);
                    tmp.getChildren().addAll(contentText, action);
                    setGraphic(tmp);
                } else if (type.equals("image")) {
                    /*
                        Show the image and add click action.
                        Download the image as a file on clicked.
                     */
                    ImageView tmp = new ImageView();
                    try {
                        BufferedImage tmpbufim = ImageIO.read(new ByteArrayInputStream(
                                Base64.getDecoder().decode(content)));
                        tmp.setImage(SwingFXUtils.toFXImage(tmpbufim, null));
                    } catch (IOException ex) {
                        System.err.print(ex);
                    }
                    action.setGraphic(tmp);
                    action.setText("");
                    setGraphic(action);
                } else {
                    /*
                        Show the text.
                        If the text contains one or more youtube link, show them.
                     */
                    VBox layout = new VBox();
                    layout.getChildren().add(contentText);
                    String youtubeStringMatch = "https://www.youtube.com";
                    if (content.contains(youtubeStringMatch)) {
                        int tmp = content.indexOf(youtubeStringMatch);
                        List<String> yturl = new LinkedList<>();
                        while (tmp != -1) {
                            int urlend = content.indexOf(" ", tmp + 1);
                            urlend = urlend == -1 ? content.length() : urlend;
                            String tmpurl = content.substring(tmp, urlend);
                            tmpurl = tmpurl.replace("watch?v=", "embed/");
                            yturl.add(tmpurl);
                            tmp = content.indexOf(youtubeStringMatch, urlend + 1);
                        }
                        for (String t : yturl) {
                            System.err.println(t);
                            WebView ytwb = new WebView();
                            ytwb.getEngine().load(t);
                            ytwb.setPrefSize(180, 144);
                            layout.getChildren().add(ytwb);
                        }
                    }
                    setGraphic(layout);
                }
            } else
                setGraphic(null);
        }
    }
}
