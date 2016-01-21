package Client;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by aalexx on 1/2/16.
 */
public class LoginPageController implements Initializable {
    @FXML private TextField account;
    @FXML private PasswordField password;
    @FXML private TextField ip;
    @FXML private TextField port;
    @FXML private Button login;
    @FXML private Button register;
    @FXML private Text message;

    public void setAccount(String account) {
        this.account.setText(account);
    }

    public void setIp(String ip) {
        this.ip.setText(ip);
    }

    public void setPassword(String password) {
        this.password.setText(password);
    }

    public void setPort(String port) {
        this.port.setText(port);
    }

    public String getAccount() {
        return account.getText();
    }

    public String getIp() {
        return ip.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public String getPort() {
        return port.getText();
    }

    public void setMessage (String message) {
        this.message.setText(message);
    }

    public void setLoginOnAction (EventHandler<ActionEvent> handler) {
        login.setOnAction(handler);
    }

    public void setRegisterOnAction (EventHandler<ActionEvent> handler) {
        register.setOnAction(handler);
    }

    private boolean _checkInformationValid () {
        if (account.getText() != null && !account.getText().equals("") &&
                password.getText() != null && !password.getText().equals("") &&
                ip.getText() != null && !ip.getText().equals("") &&
                port.getText() != null && !port.getText().equals(""))
            return true;
        return false;
    }

    private void checkButtonValid () {
        if (_checkInformationValid()) {
            login.setDisable(false);
            register.setDisable(false);
        } else {
            login.setDisable(true);
            register.setDisable(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Pattern DOT = Pattern.compile("^[^\\.]*$");
        Pattern IP = Pattern.compile("^[0-9\\.]*$");
        Pattern PORT = Pattern.compile("^[1-9]*$");
        ChangeListener dotListener = (observable, oldValue, newValue) -> {
            if (!DOT.matcher((String)newValue).matches()) {
                ((Property)observable).setValue(oldValue);
            }
            checkButtonValid();
        };
        ChangeListener ipListener = (observable, oldValue, newValue) -> {
            if (!IP.matcher((String)newValue).matches()) {
                ((Property)observable).setValue(oldValue);
            }
            checkButtonValid();
        };
        ChangeListener portListener = (observable, oldValue, newValue) -> {
            if (!PORT.matcher((String)newValue).matches()) {
                ((Property)observable).setValue(oldValue);
            }
            checkButtonValid();
        };
        account.textProperty().addListener(dotListener);
        password.textProperty().addListener(dotListener);
        ip.textProperty().addListener(ipListener);
        port.textProperty().addListener(portListener);
        login.setDisable(true);
        register.setDisable(true);
    }
}
