package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
from http://www.studytrails.com/java-io/non-blocking-io-multiplexing.jsp
need modification
*/

public static class ConnectionUtility {
    private String server_ip;
    private int server_port;

    public void setupConnection(String ip, int port){
        server_ip = ip;
        server_port = port;
    }

    public void send(JSONObject json) {
        SocketChannel channel = SocketChannel.open();

        // we open this channel in non blocking mode
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(server_ip, server_port));

        while (!channel.finishConnect()) {
            // System.out.println("still connecting");
        }

        CharBuffer buffer = CharBuffer.wrap(json.toString());
        while (buffer.hasRemaining()) {
            try {
                channel.write(Charset.defaultCharset().encode(buffer));
            }
            catch (Exception e) {
            }
        }

        ByteBuffer bufferA = ByteBuffer.allocate(20);
        int count = 0;
        String message = "";
        try {
            while ((count = channel.read(bufferA)) > 0) {
                // flip the buffer to start reading
                bufferA.flip();
                message += Charset.defaultCharset().decode(bufferA);

            }
        }
        catch(Exception e){
        }
        channel.close();

        json = new JSONObject(message);
        //System.out.println(message);
        //handle json
    }
}