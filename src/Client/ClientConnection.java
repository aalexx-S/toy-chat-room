package Client;

import Client.EventHandler.Handler;
import Shared.*;
import org.json.JSONObject;

import java.io.File;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ClientConnection {
    private String server_ip;
    private int server_port;
    private Connection serverConnection;
    private Queue<File> sendFileQueue = new ConcurrentLinkedDeque<>();
    private static ClientConnection sharedInstance;
    private Handler handler;

    public static ClientConnection getSharedInstance () {
        if (sharedInstance == null) {
            synchronized (ClientConnection.class) {
                if (sharedInstance == null) {
                    sharedInstance = new ClientConnection();
                }
            }
        }
        return sharedInstance;
    }

    public void setHandler (Handler handler) {
        this.handler = handler;
    }

    public void setIpPort(String ip, int port){
        this.server_ip = ip;
        this.server_port = port;
    }

    public void startSendingFile (int port) {
        Thread fileSending = new Thread(() -> {
            if (! sendFileQueue.isEmpty()) {
                try {
                    Socket socket = new Socket(server_ip, port);
                    FileWriteConnection connection = new FileWriteConnection(socket);
                    connection.send(sendFileQueue.poll());
                    connection.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fileSending.start();
    }

    public void startRequestFile (int port, String fileName) {
        Queue<byte[]> fileReadQueue = new ConcurrentLinkedDeque<>();
        try {
            Socket socket = new Socket(server_ip, port);
            FileReadConnection connection = new FileReadConnection(socket, fileReadQueue);
            connection.start();
            Thread readFile = new Thread(() -> {
                while (true) {
                    if (! fileReadQueue.isEmpty()) {
                        String home = System.getProperty("user.home");
                        Utility.byteToFile(fileReadQueue.poll(), home + "/Downloads/" + fileName);
                        break;
                    }
                }
            });
            readFile.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addQueueingSendFile (File file) {
        this.sendFileQueue.add(file);
    }

    public void send (Map<String, String> message) {
        try {
            serverConnection.send(new MapToJSONFactory().create(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start () {
        Queue<JSONObject> readQueue = new ConcurrentLinkedDeque<>();
        Thread readingThread = new Thread(() -> {
            while (true) {
                if (! readQueue.isEmpty()) {
                    JSONObject obj = readQueue.poll();
                    if (obj.getString("instruction").equals("LOGOUT"))
                        continue;
                    ServerClientMessage msg = new JSONToServerClientMessageFactory().create(obj);
                    handler.handle(msg);
                }
            }
        });
        readingThread.start();
        try {
            Socket serverSocket = new Socket(server_ip, server_port);
            serverConnection = new Connection(serverSocket, readQueue);
            serverConnection.start();
        } catch (Exception e) {
            Client.getLoginPageController().setMessage("Cannot connect to Server");
            readingThread.interrupt();
        }
    }
}
