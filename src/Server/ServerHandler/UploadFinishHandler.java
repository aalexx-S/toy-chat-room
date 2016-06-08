package Server.ServerHandler;

import Server.DatabaseManager.FileManager;
import Server.ServerAction.ReceiveMessageAction;
import Server.ServerAction.UploadFinishAction;

import java.util.Map;

/**
 * Created by Tony on 2016/1/21.
 */
public class UploadFinishHandler extends ServerHandler {
    public UploadFinishHandler(ServerHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean doAction(Map<String, String> message) {
        if (message.get("instruction").equals("FILE_UPLOAD_FINISH")) {
            message.put("type", "file");
            String[] split_id = message.get("content").split("_");
            message.put("file_id", split_id[0]);
            UploadFinishAction uploadFinishAction = new UploadFinishAction(message);
            uploadFinishAction.doAction();
            return true;
        }
        else if (message.get("instruction").equals("IMAGE_PREVIEW")) {
            message.put("type", "image");
            ReceiveMessageAction receiveMessageAction = new ReceiveMessageAction(message);
            receiveMessageAction.doAction();
            return true;
        }
        else
            return false;
    }
}
