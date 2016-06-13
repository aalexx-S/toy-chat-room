package Shared;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Connection extends Thread {
    private volatile Queue<JSONObject> readQueue;
    private volatile Queue<JSONObject> writeQueue = new ConcurrentLinkedDeque<>();
    private Thread writeThread;
    private Socket socket;
    private InputStream ois;
    private OutputStream oos;

    public Connection(Socket socket, Queue<JSONObject> readQueue) {
        this.readQueue = readQueue;
        this.socket = socket;
        try {
            oos = socket.getOutputStream();
            ois = socket.getInputStream();
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
                        if (msg.toString().length() <= 128)
                            System.err.println("[send] " + msg);
                        else
                            System.err.println("[send]" + msg.toString().substring(0, 64) + "...");
                        byte[] messageBytes = msg.toString().getBytes();
                        int size = messageBytes.length;
                        ByteBuffer bb = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                        byte[] bytes = bb.putInt(size).array();
                        oos.write(bytes);
                        oos.write(messageBytes);
                        oos.flush();
                    } catch (Exception e) {
                        System.err.println("[Log] Disconnected. Write thread terminated.");
                    }
                }
            }
        });
        writeThread.start();
        int bufferSize = 65536;
        int currentRead;
        try {
            byte[] bytes = new byte[bufferSize];
            int size, now, already;
            while (true) {
                now = ois.read(bytes);
                size = ByteBuffer.wrap(bytes, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
                if (now < 0)
                    throw new Exception("EOF");
                byte[] msgByte = new byte[size];
                System.arraycopy(bytes, 4, msgByte, 0, now-4);
                already = now - 4; now = 0;

                while (already + now < size) {
                    currentRead = ois.read(bytes, now, bufferSize - now);
                    if (currentRead < 0) {
                        throw new Exception("EOF");
                    }

                    now += currentRead;
                    if (now > bufferSize - 10) {
                        System.arraycopy(bytes, 0, msgByte, already, now);
                        already += now;
                        now = 0;
                    }
                }
                System.arraycopy(bytes, 0, msgByte, already, now);

                JSONObject msg = new JSONObject(new String (msgByte));
                readQueue.add(msg);
                System.err.println("[recv] " + msg);
            }
        }
        catch (Exception e) {
            System.err.println("[Log] Disconnected. Read thread terminated.");
            JSONObject logoutMsg = new JSONObject();
            try {
                logoutMsg.put("instruction", "DISCONNECT");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            readQueue.add(logoutMsg);
            writeThread.interrupt();
            interrupt();
        }
    }
}
