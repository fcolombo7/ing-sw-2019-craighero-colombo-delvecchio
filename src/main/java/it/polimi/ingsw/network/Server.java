package it.polimi.ingsw.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements RMIServerHandler {
    private static final int PORT = 12345;
    private static final String SERVER_NAME = "adrenaline";

    private ServerSocket serverSocket;

    private ExecutorService executor = Executors.newFixedThreadPool(128);

    private Map<String, ClientConnection> waitingConnection = new HashMap<>();

    private Map<String,RMIClientHandler> rmiClients;

    public Server() throws IOException, AlreadyBoundException {
        this.serverSocket = new ServerSocket(PORT);
        //RMIServerHandler stub=(RMIServerHandler) UnicastRemoteObject.exportObject(this,0);

        // Bind the remote object's stub in the registry
        //Registry registry = LocateRegistry.getRegistry();
        //registry.bind(SERVER_NAME, stub);
    }

    @Override
    public synchronized  boolean login(String clientName) throws RemoteException {
        Registry registry= LocateRegistry.getRegistry();
        try {
            if(!firstLogin(clientName)) return false;

            for (String str:rmiClients.keySet()) {
                if(str.equalsIgnoreCase(clientName))return false;
            }

            RMIClientHandler stub=(RMIClientHandler) registry.lookup(clientName);
            rmiClients.put(clientName,stub);

            RMIClientConnection rmiConnection = new RMIClientConnection(stub,this);

        } catch (NotBoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public synchronized void deregisterConnection(ClientConnection c) {
        for(String key:waitingConnection.keySet()){
            if(waitingConnection.get(key)==c){
                System.out.println(key + " has closed the connection..");
                waitingConnection.remove(key);
                break;
            }
        }
    }


    public synchronized void addInWaitingRoom(ClientConnection c, String name){
        if(firstLogin(name)) {
            waitingConnection.put(name, c);
            System.out.println(name+" is waiting...");
        }
    }

    protected synchronized boolean firstLogin(String name){
        for(String str:waitingConnection.keySet()){
            if(str.equalsIgnoreCase(name)) return false;
        }
        return true;
    }

    public void runSocket(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.err.println("Socket connection error. "+ e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Server server;
        try {
            server = new Server();
            server.runSocket();
            System.out.println("Server ready");
        } catch (IOException e) {
            System.err.println("Cannot initialized the server due to socket error. " + e.getMessage());
        } catch (AlreadyBoundException e) {
            System.err.println("Cannot initialized the server due to RMI registry error: " + e.getMessage());
        }
    }

}
