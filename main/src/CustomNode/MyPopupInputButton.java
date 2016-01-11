package CustomNode;

import javafx.beans.NamedArg;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 1/10/16.
 */
public class MyPopupInputButton extends HBox implements Initializable {
    @FXML
    private Button button;
    private String defaultString; //default string in the input box
    private String text;// = "confirm";
    private String title ;//= "Default Title";
    private String headerText;// = "Default header text.";
    private String contentText ;//= "Default content.";
    private boolean defaultButton = false;
    private Callback<String, Void> confirmAction = param -> null;

    public MyPopupInputButton(@NamedArg("text") String text,
                              @NamedArg("defaultString") String defaultString,
                              @NamedArg("title") String title,
                              @NamedArg("headerText") String headerText,
                              @NamedArg("contentText") String contentText,
                              @NamedArg("defaultButton") boolean isDefaultButton) {
        this.text = text;
        this.defaultString = defaultString;
        this.title = title;
        this.headerText = headerText;
        this.contentText = contentText;
        this.defaultButton = isDefaultButton;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyPopupInputButton.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setText (String text) {
        this.text = text;
        button.setText(this.text);
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public void setHeaderText (String headerText) {
        this.headerText = headerText;
    }

    public void setContentText (String contentText) {
        this.contentText = contentText;
    }

    public void setOnConfirm (Callback<String, Void> confirmAction) {
        this.confirmAction = confirmAction;
    }

    @FXML
    protected void onButtonClicked () {
        TextInputDialog confirmationAlert = new TextInputDialog(defaultString);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(headerText);
        confirmationAlert.setContentText(contentText);
        Optional<String> result = confirmationAlert.showAndWait();
        result.ifPresent(entered -> confirmAction.call(entered));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText(text);
        setTitle(title);
        setHeaderText(headerText);
        setContentText(contentText);
        button.setDefaultButton(defaultButton);
    }
}
