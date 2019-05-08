package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.SocketClientConnection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    private Server server;

    private ServerSocket serverSocket;

    public SocketServer(Server server){
        this.server=server;
    }

    public void startServer(int port) throws ServerException {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Socket server started.");
            new SocketManager(server).start();
        } catch (IOException e) {
            throw new ServerException("Error occurs while starting the socket server (IOException):\n" + e.getMessage());
        }
    }

    private class SocketManager extends Thread{
        private ExecutorService executor = Executors.newFixedThreadPool(128);
        private Server server;
        SocketManager(Server server){
            this.server=server;
        }
        @Override
        public void run() {
            while(true){
                try {
                    Socket socket = serverSocket.accept();
                    SocketClientConnection client= new SocketClientConnection(socket,server);
                    executor.submit(client);
                } catch (IOException e) {
                    System.out.println("Error occurs while accepting a socket connection:\n" + e.getMessage());
                    break;
                }
            }
        }
    }
}
