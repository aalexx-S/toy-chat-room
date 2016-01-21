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
public class SendFileAction extends ServerAction {

    public SendFileAction(Map<String, String> message) {
        super(message);
    }

    @Override
    public void doAction() {
        FileManager fileManager = new FileManager();
        String fileName = fileManager.queryFileName(message.get("name"));
        File requested = new File("./uploadedFiles/" + message.get("name") + "_uploadedFile");
        int filePort = ServerConnection.getInstance().startListeningForRequestFile(requested);
        ServerClientMessage ackMessage = ServerClientMessageBuilder.create()
                                        .setInstruction(510)
                                        .setRoomId(filePort)
                                        .setContent(fileName)
                                        .build();
        ServerConnection.getInstance().send(message.get("sender_name"), ackMessage);
    }
}
