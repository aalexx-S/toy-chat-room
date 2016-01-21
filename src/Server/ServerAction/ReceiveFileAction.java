package Server.ServerAction;

import Server.DatabaseManager.FileManager;
import Server.ServerConnection;
import Shared.ServerClientMessage;
import Shared.ServerClientMessageBuilder;

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
        int filePort = ServerConnection.getInstance().startListeningForReceiveFile("./uploadedFiles/" + Integer.toString(token) + "_uploadedFile");
        ServerClientMessage ackMessage = ServerClientMessageBuilder.create()
                                        .setInstruction(500)
                                        .setRoomId(filePort)
                                        .build();
        ServerConnection.getInstance().send(message.get("sender_name"), ackMessage);
    }
}
