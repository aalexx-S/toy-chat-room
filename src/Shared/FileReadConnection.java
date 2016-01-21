package Shared;
import java.io.File;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Queue;

public class FileReadConnection extends Thread {
    private volatile Queue<byte[]> readQueue;
    private Socket socket;
    private ObjectInputStream ois;

    public FileReadConnection(Socket socket, Queue<byte[]> readQueue) {
        this.readQueue = readQueue;
        this.socket = socket;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    public void run() {
        try {
            byte[] data = (byte[]) ois.readObject();
            System.err.println("[recv file] " + data);
            readQueue.add(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
