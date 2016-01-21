package Shared;
import java.io.File;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class FileWriteConnection extends Thread {
    private volatile Queue<File> writeQueue = new ConcurrentLinkedDeque<>();
    private Socket socket;
    private ObjectOutputStream oos;

    public FileWriteConnection (Socket socket) {
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(File msg) throws InterruptedException {
        writeQueue.add(msg);
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    public void run() {
        while (true) {
            if (! writeQueue.isEmpty()) {
                try {
                    File msg = writeQueue.poll();
                    byte[] data = Utility.fileToByte(msg);
                    System.err.println("[send file] " + msg);
                    oos.writeObject(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }
}
