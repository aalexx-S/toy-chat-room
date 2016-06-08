package Server;

import Client.EventHandler.Handler;
import Server.DatabaseManager.*;
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
        if (args.length < 1 || args.length > 2) {
            System.err.println("Usage: java -jar Server.jar [port] [ip (optional)]");
            return;
        }

        new AccountManager().createTable();
        new FileManager().createTable();
        new HistoricalMessageManager().createTable();
        new NotifyManager().createTable();
        new RoomInfoManager().createTable();
        new RoomListManager().createTable();

        int port = Integer.parseInt(args[0]);
        portString = args[0];
        // get ip
        if (args.length == 2)
            ipString = args[1];
        else {
            boolean f = false;
            try {
                for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = (NetworkInterface) en.nextElement();
                    for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
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
        }
        // print basic connection information
        System.out.println("=======================");
        System.out.println("=    Server Start     =");
        System.out.println("=======================");
        System.out.printf(" Ip: %s\n Port: %s\n", ipString, portString);
        System.out.println("=======================");

        LogoutHandler logoutHandler = new LogoutHandler(null);
        TimeHandler generalHandler = new TimeHandler(
                                new AddPersonHandler(
                                new CreateChatRoomHandler(
                                new CreateMeetingRoomHandler(
                                new LeaveRoomHandler(
                                new OpenRoomHandler(
                                new RegisterHandler(
                                new RequestFileHandler(
                                new SendFileHandler(
                                new SendMessageHandler(
                                logoutHandler
                                ))))))))));
        ServerConnection serverConnection = ServerConnection.getInstance();

        ServerHandler authenticateHandler = new TimeHandler(
                                              new RegisterHandler(
                                              new LoginHandler(
                                              new UploadFinishHandler(
                                              new DisconnectHandler(null)))));

        serverConnection.setIpPort(ipString, port);
        serverConnection.setGeneralHandler(generalHandler);
        serverConnection.setAuthenticateHandler(authenticateHandler);

        serverConnection.start();
    }
}
