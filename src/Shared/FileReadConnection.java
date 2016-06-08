package Shared;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Queue;

public class FileReadConnection extends Thread {
    private volatile Queue<byte[]> readQueue;
    private Socket socket;
    private InputStream ois;

    public FileReadConnection(Socket socket, Queue<byte[]> readQueue) {
        this.readQueue = readQueue;
        this.socket = socket;
        try {
           ois = socket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    public void run() {
        int bufferSize = 65536;
        try {
            int now, already, currentRead;
            byte[] buffer = new byte[bufferSize];
            now = ois.read(buffer);
            int size = ByteBuffer.wrap(buffer, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            byte[] data = new byte[size];
            System.arraycopy(buffer, 4, data, 0, now - 4);
            already = now -4; now = 0;

            while (already + now < size) {
                currentRead = ois.read(buffer, now, bufferSize - now);
                if (currentRead < 0) {
                    throw new Exception("EOF");
                }
                now += currentRead;
                if (now > bufferSize - 10) {
                    System.arraycopy(buffer, 0, data, already, now);
                    already += now;
                    now = 0;
                }
            }
            System.arraycopy(buffer, 0, data, already, now);
            System.err.println("[recv file] " + data);
            readQueue.add(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
