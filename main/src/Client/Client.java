package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/10/16.
 */
public class Client extends Application {
    private Stage stage;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        Parent root = loader.load();
        LoginPageController controller = loader.getController();
        /*
         * do login/register stuff here
         */
        controller.setLoginOnAction(event -> loginSuccess());


        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public void loginSuccess () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        MainPageController controller = loader.getController();
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
         * setup core and other functionality here
         */
        // test
        Map<String, String> ma1 = new HashMap<>();
        ma1.put("id", "00001");
        ma1.put("name", "lalala");
        ma1.put("status", "online");
        Map<String, String> ma2 = new HashMap<>();
        ma2.put("id", "00002");
        ma2.put("name", "mullll");
        ma2.put("status", "stuffffffff");
        List<Map<String, String>> stuff = new ArrayList<>();
        stuff.add(ma1);
        stuff.add(ma2);
        controller.setSingleTableList(stuff);
        controller.setMultipleTableList(stuff);
        controller.setSingleOnEnterAction(param -> {
            ChatRoom r = new ChatRoom(param, "single");
            List<Map<String, String>> ssss = new ArrayList<>();
            Map<String, String> mb1 = new HashMap<>();
            mb1.put("name", "apple");
            mb1.put("type", "file");
            mb1.put("content", "100 kb");
            mb1.put("time", "10000020");
            mb1.put("id", "sdfsfsdf");
            ssss.add(mb1);
            r.setMessages(ssss);
            r.setOnDownload(param1 -> {
                System.out.println("Oh my god it works! " + param1);
                return null;
            });
            r.show();
            return null;
        });
        controller.setMultipleOnEnterAction(param -> {
            ChatRoom r = new ChatRoom(param, "multiple");
            r.show();
            return null;
        });

        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(root, 800, 600);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(root);
        }
        stage.sizeToScene();
    }
}
