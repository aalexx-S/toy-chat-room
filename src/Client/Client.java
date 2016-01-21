package Client;

import Client.EventHandler.*;
import Shared.ServerClientMessage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aalexx on 1/10/16.
 */
public class Client extends Application {
    private Stage stage;
    private Parent mainRoot;
    private static LoginPageController loginPageController;
    private static MainPageController mainPageController;
    private static Handler handler;
    public static LoginPageController getLoginPageController () {
        return loginPageController;
    }
    public static MainPageController getMainPageController () {
        return mainPageController;
    }
    public static void handleMessage (ServerClientMessage message) {
        handler.handle(message);
    }

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load login page
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        Parent root = loader.load();
        loginPageController = loader.getController();
        // load main page
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
        try {
            mainRoot = mainLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        mainPageController = mainLoader.getController();
        /* on close event */
        stage.setOnCloseRequest(event -> {
            GuiCloseEventHandler handle = new GuiCloseEventHandler(stage, event);
            handle.setOnCloseAction(windowEvent -> {
                // do closing stuff here
                System.exit(0);
            });
            handle.onClose();
        });
        /*
         * do core stuff here
         */
        // handlers
        LoginSuccessHandler loginSuccess = new LoginSuccessHandler(null);
        loginSuccess.setClient(this);
        handler = new AckRequestFileHandler(
                  new AckSendFileHandler(
                  new AddRoomMessageHandler(
                  new CreateChatRoomResponseHandler(
                  new LoginFailedHandler(
                  new QueryPersonHandler(
                  new RoomAddPersonResponseHandler(
                  new RoomListUpdateHandler(
                  new SyncRoomMessageHandler(loginSuccess)))))))));
        loginPageController.setRegisterOnAction(event -> {
            Map<String, String> msg = new HashMap<String, String>();
            msg.put("instruction", "REGISTER");
            msg.put("account", loginPageController.getAccount());
            msg.put("password", loginPageController.getPassword());
            // todo send
        });
        loginPageController.setLoginOnAction(event -> {
            Map<String, String> msg = new HashMap<String, String>();
            msg.put("instruction", "LOGIN");
            msg.put("account", loginPageController.getAccount());
            msg.put("password", loginPageController.getPassword());
            // todo send
        });


        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public void loginSuccess () {
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(mainRoot, 800, 600);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(mainRoot);
        }
        stage.sizeToScene();
    }
}
