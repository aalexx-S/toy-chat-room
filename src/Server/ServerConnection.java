package Server;
import Server.ServerHandler.ServerHandler;
import Shared.*;
import org.json.JSONObject;

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
	private Map<Integer, Connection> sequenceConnectionMapping = new ConcurrentHashMap<>();
    private volatile Map<Queue<JSONObject>, Integer> readQueueSequenceMapping = new ConcurrentHashMap<>();
    private volatile Map<String, Integer> nameSequenceMapping = new ConcurrentHashMap<>();
    private volatile Map<Integer,String> sequenceNameMapping = new ConcurrentHashMap<>();
	private int totalAccept = 0;
    private int port;
    private ServerHandler handler;
    private String ip;
    private static ServerConnection sharedInstanced;

    private ServerConnection () {
    }

    public static ServerConnection getSharedInstanced () {
        if (sharedInstanced == null) {
            synchronized (ServerConnection.class) {
                if (sharedInstanced == null) {
                    sharedInstanced = new ServerConnection();
                }
            }
        }
        return sharedInstanced;
    }

    public void addName(String name, int number){
        nameSequenceMapping.put(name,number);
        sequenceNameMapping.put(number,name);
    }

    public void removeName(String name){
        int number = nameSequenceMapping.get(name);
        nameSequenceMapping.remove(name);
        sequenceNameMapping.remove(number);
    }

    public void setHandler (ServerHandler handler) {
        this.handler = handler;
    }

    public void setIpPort (String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

	public void send (Integer sequence, Map<String, String> message) {
        try {
            sequenceConnectionMapping.get(sequence).send(new MapToJSONFactory().create(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send (String name, Map<String, String> message) {
        try {
            sequenceConnectionMapping.get(nameSequenceMapping.get(name)).send(new MapToJSONFactory().create(message));
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
                    if (i.size() != 0) {
						try {
                            int number = readQueueSequenceMapping.get(i);
                            String name = null;
                            if (sequenceNameMapping.containsKey(number))
                                name = sequenceNameMapping.get(number);
							Map<String, String> msg = new JSONToMapFactory().create(i.poll(), name, Integer.toString(number));
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
						Utility.byteToFile(obj, fileName);
                        // todo inform
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
		Queue<byte[]> readQueue = new ConcurrentLinkedDeque<>();
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
