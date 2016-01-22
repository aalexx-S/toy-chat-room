package Client.EventHandler;

import Client.Client;
import Shared.ServerClientMessage;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Created by aalexx on 1/22/16.
 */
public class FileRequestFinishHandler extends Handler {
    public FileRequestFinishHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean doAction (ServerClientMessage msg) {
        if (msg.getInstruction() == 600) { // download finish
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(Client.getRoot());
                alert.setTitle("File Download Finish");
                alert.setHeaderText("Download Finish");
                alert.setContentText(msg.getContent() + " has downloaded finish!!");
                alert.show();
            });
            return true;
        }
        return false;
    }
}
