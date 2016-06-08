package Shared;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Connection extends Thread {
    private volatile Queue<JSONObject> readQueue;
    private volatile Queue<JSONObject> writeQueue = new ConcurrentLinkedDeque<>();
    private Thread writeThread;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Connection(Socket socket, Queue<JSONObject> readQueue) {
        this.readQueue = readQueue;
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(JSONObject msg) throws InterruptedException {
        writeQueue.add(msg);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        writeThread.interrupt();
    }

    public void run() {
        writeThread = new Thread(() -> {
            while (true) {
                if (! writeQueue.isEmpty()) {
                    try {
                        JSONObject msg = writeQueue.poll();
                        System.err.println("[send] " + msg);
                        oos.writeObject(msg.toString());
                    } catch (Exception e) {
                        System.err.println("[Log] Client logout.");
                    }
                }
            }
        });
        writeThread.start();
        try {
            while (true) {
                JSONObject msg = new JSONObject((String) ois.readObject());
                readQueue.add(msg);
                System.err.println("[recv] " + msg);
            }
        }
        catch (Exception e) {
            System.err.println("[Log] Client disconnect.");
            JSONObject logoutMsg = new JSONObject();
            try {
                logoutMsg.put("instruction", "DISCONNECT");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            readQueue.add(logoutMsg);
            interrupt();
        }
    }
}
