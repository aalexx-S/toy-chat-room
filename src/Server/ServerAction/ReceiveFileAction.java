package Server.ServerAction;

import Server.DatabaseManager.FileManager;

import java.io.File;
import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class ReceiveFileAction extends ServerAction {

    public ReceiveFileAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        new File("./uploadedFiles").mkdirs();
        FileManager fileManager = new FileManager();
        int token = fileManager.add(message);
        //todo
        //construct ack send file message
        //send message to room with file token and put message in historicalMessage
    }
}
