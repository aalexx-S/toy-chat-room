package Server;
import Server.ServerHandler.ServerHandler;
import Shared.*;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnection {
	private Thread listenThread;
    private Queue<JSONObject> serverQueue = new ConcurrentLinkedDeque<>();
	private Map<Integer, Connection> sequenceConnectionMapping = new ConcurrentHashMap<>();
    private volatile Map<Queue<JSONObject>, Integer> readQueueSequenceMapping = new ConcurrentHashMap<>();
    private volatile Map<String, Integer> nameSequenceMapping = new ConcurrentHashMap<>();
    private volatile Map<Integer,String> sequenceNameMapping = new ConcurrentHashMap<>();
	private int totalAccept = 1;
    private int port;
    private ServerHandler handler;
	private ServerHandler generalHandler;
	private ServerHandler authenticateHandler;
	private ServerSecurity security = new ServerSecurity();
    private String ip;
    private static ServerConnection sharedInstanced;

    private ServerConnection () {
        readQueueSequenceMapping.put(serverQueue, 0);
    }

    public static ServerConnection getInstance () {
        if (sharedInstanced == null) {
            synchronized (ServerConnection.class) {
                if (sharedInstanced == null) {
                    sharedInstanced = new ServerConnection();
                }
            }
        }
        return sharedInstanced;
    }

	public ServerSecurity getSecurity() {
		return security;
	}

    public void addName(String name, int number){
        nameSequenceMapping.put(name,number);
        sequenceNameMapping.put(number,name);
    }

    public void removeName(String name){
        if (name == null || !nameSequenceMapping.containsKey(name) || !sequenceNameMapping.containsKey(name)) {
            return;
        }
        int number = nameSequenceMapping.get(name);
        nameSequenceMapping.remove(name);
        sequenceNameMapping.remove(number);
    }

    private void setHandler (ServerHandler handler) {
        this.handler = handler;
    }

	public void setGeneralHandler (ServerHandler handler) {this.generalHandler = handler;}

	public void setAuthenticateHandler (ServerHandler handler) {this.authenticateHandler = handler;}

    public void setIpPort (String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

	public void send (Integer sequence, ServerClientMessage message) {
        if (!sequenceConnectionMapping.containsKey(sequence))
            return;
        try {
            sequenceConnectionMapping.get(sequence).send(new ServerClientMessageToJSONFactory().create(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send (String name, ServerClientMessage message) {
        if (!nameSequenceMapping.containsKey(name))
            return;
        try {
            sequenceConnectionMapping.get(nameSequenceMapping.get(name)).send(new ServerClientMessageToJSONFactory().create(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start () {
		listenThread = new Thread(new Runnable() {
			private int port;
			public Runnable setPort(int port) {
				this.port = port;
				return this;
			}
			@Override
			public void run() {
				ServerSocket serverSocket = null;
				ExecutorService threadExecutor = Executors.newCachedThreadPool();
				try {
					serverSocket = new ServerSocket(this.port, 50, InetAddress.getByName(ip));
					System.err.println("Server listening requests on ip " + serverSocket.getInetAddress().getHostAddress());
					while (true) {
						Socket socket = serverSocket.accept();
						Queue<JSONObject> readQueue = new ConcurrentLinkedDeque<JSONObject>();
						Connection connection = new Connection(socket, readQueue);
						threadExecutor.execute(connection);
						sequenceConnectionMapping.put(totalAccept, connection);
						readQueueSequenceMapping.put(readQueue, totalAccept++);
					}

				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.setPort(port));
		listenThread.start();
		Thread readThread = new Thread(() -> {
            while (true) {
                for (Queue<JSONObject> i : readQueueSequenceMapping.keySet()) {
                    if (! i.isEmpty()) {
						try {
                            int number = readQueueSequenceMapping.get(i);
                            String name = null;
                            if (sequenceNameMapping.containsKey(number))
                                name = sequenceNameMapping.get(number);
							Map<String, String> msg = new JSONToMapFactory().create(i.poll(), name, Integer.toString(number));

							if (security.tokenValid(number, Integer.valueOf(msg.get("token"))))
								setHandler(generalHandler);
							else
								setHandler(authenticateHandler);

							handler.handle(msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }
                }
            }
        });
		readThread.start();
	}
	/*
	 * Pass file name (including path, for instance "./some_folder/file_name") in.
	 *
	 * Return port listening for receiving file.
	 */
	public Integer startListeningForReceiveFile (String fileName) {
		Integer port = findFreePort();
		Queue<byte[]> readQueue = new ConcurrentLinkedDeque<>();
		Thread fileReceive = new Thread(new Runnable() {
			private int port;
			private String fileName;
			private Queue<byte[]> readQueue;
			public Runnable setParam(int port, String fileName, Queue<byte[]> readQueue) {
				this.port = port;
				this.fileName = fileName;
				this.readQueue = readQueue;
				return this;
			}
			@Override
			public void run() {
				ServerSocket serverSocket = null;
				ExecutorService threadExecutor = Executors.newCachedThreadPool();
				try {
					serverSocket = new ServerSocket(this.port, 50, InetAddress.getByName(ip));
					System.err.println("Server listening file receive on ip " + serverSocket.getInetAddress().getHostAddress());
					// one request only allow one client
					Socket socket = serverSocket.accept();
					FileReadConnection connection = new FileReadConnection(socket, readQueue);
					threadExecutor.execute(connection);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.setParam(port, fileName, readQueue));
		fileReceive.start();
		Thread readThread = new Thread(() -> {
			while (true) {
				if (! readQueue.isEmpty()) {
					byte[] obj = readQueue.poll();
					try {
						File file = Utility.byteToFile(obj, fileName);
						/*BufferedImage image = ImageIO.read(file);
						if (image != null) {
							ImageProcessor imageProcessor = new ImageProcessor();
							int width = 0;
							int height = 0;
							imageProcessor.createResizeCopy(image, width, height);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(image, "png", baos);
							baos.flush();
							String encodedImage = new String(baos.toByteArray());
							baos.close();

							JSONObject inform = new JSONObject();
							inform.put("instruction", "IMAGE_PREVIEW");
							inform.put("content", encodedImage);
						}*/
                        JSONObject inform = new JSONObject();
                        inform.put("instruction", "FILE_UPLOAD_FINISH");
                        inform.put("content", file.getName());
                        serverQueue.add(inform);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		});
		readThread.start();
		return port;
	}

	/*
	 * Pass the file object in.
	 *
	 * Return the port listening for requesting.
	 */
	public Integer startListeningForRequestFile (File obj) {
		int port = findFreePort();
		Thread fileRequest = new Thread(new Runnable() {
			private int port;
			public Runnable setParam(int port) {
				this.port = port;
				return this;
			}
			@Override
			public void run() {
				ServerSocket serverSocket = null;
				ExecutorService threadExecutor = Executors.newCachedThreadPool();
				try {
					serverSocket = new ServerSocket(this.port, 50, InetAddress.getByName(ip));
					System.err.println("Server listening file request on ip " + serverSocket.getInetAddress().getHostAddress());
					// one request only allow one client
					Socket socket = serverSocket.accept();
					FileWriteConnection connection = new FileWriteConnection(socket);
					threadExecutor.execute(connection);
					try {
						connection.send(obj);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.setParam(port));
		fileRequest.start();
		return port;
	}

	private Integer findFreePort () {
		int port = 0;
		try(ServerSocket server = new ServerSocket(0);){
			port = server.getLocalPort();
		}catch(Exception e){
			System.err.println("unable to find a free port");
			return null;
		}
		return port;
	}


}
