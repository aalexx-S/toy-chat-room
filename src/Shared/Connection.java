package Shared;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Connection extends Thread {
    private volatile Queue<JSONObject> readQueue;
    private volatile Queue<JSONObject> sendQueue;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Thread writeThread;

    public Connection(Socket socket, Queue<JSONObject> readQueue) {
        this.readQueue = readQueue;
        this.sendQueue = new ConcurrentLinkedDeque<>();
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(JSONObject msg) throws InterruptedException {
        sendQueue.add(msg);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        writeThread.interrupt();
    }

    public void run() {
        writeThread = new Thread(() -> {
            try {
                while (true) {
                    if (sendQueue.size() != 0)
                        oos.writeObject(sendQueue.poll().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                interrupt();
            }
        });
        writeThread.start();
        try {
            while (true) {
                JSONObject msg = new JSONObject((String) ois.readObject());
                readQueue.add(msg);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            interrupt();
        }
    }
}
