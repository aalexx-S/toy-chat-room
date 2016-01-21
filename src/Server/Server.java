package Server;

import Server.ServerHandler.*;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by aalexx on 1/18/16.
 */
public class Server {
    private static String ipString = "";
    private static String portString = "";

    public static void main (String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar Server.jar [port]");
            return;
        }
        int port = Integer.parseInt(args[0]);
        portString = args[0];
        // get ip
        boolean f = false;
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress().toString();
                        ipString = inetAddress.getHostAddress();
                        f = true;
                        break;
                    }
                }
                if (f)
                    break;
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        // print basic connection information
        System.out.println("=======================");
        System.out.println("=    Server Start     =");
        System.out.println("=======================");
        System.out.printf(" Ip: %s\n Port: %s\n", ipString, portString);
        System.out.println("=======================");

        LogoutHandler logoutHandler = new LogoutHandler(null);
        TimeHandler handler = new TimeHandler(
                                new AddPersonHandler(
                                new CreateChatRoomHandler(
                                new CreateMeetingRoomHandler(
                                new LeaveRoomHandler(
                                new LoginHandler(
                                new OpenRoomHandler(
                                new RegisterHandler(
                                new RequestFileHandler(
                                new SendFileHandler(
                                new SendMessageHandler(
                                new UploadFinishHandler(
                                logoutHandler
                                ))))))))))));

        ServerConnection serverConnection = ServerConnection.getInstance();

        serverConnection.setIpPort(ipString, port);
        serverConnection.setHandler(handler);

        serverConnection.start();
    }
}
