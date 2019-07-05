package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utils.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the socket server for the game
 */
public class SocketServer {

    /**
     * This attribute represents the server
     */
    private Server server;

    /**
     * This attribute represents the server socket
     */
    private ServerSocket serverSocket;

    /**
     * This constructor initializes the server socket
     * @param server represents the server
     */
    public SocketServer(Server server){
        this.server=server;
    }

    /**
     * This method starts the socket server
     * @param port represents the socket port
     * @throws ServerException
     */
    public void startServer(int port) throws ServerException {
        try {
            serverSocket = new ServerSocket(port);
            Logger.logAndPrint("Socket server started.");
            new SocketManager(server).start();
        } catch (IOException e) {
            throw new ServerException("Error occurs while starting the socket server (IOException):\n" + e.getMessage());
        }
    }

    /**
     * This class extends Thread and overrides the run method
     */
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
                    Logger.logAndPrint("Error occurs while accepting a socket connection:\n" + e.getMessage());
                    break;
                }
            }
        }
    }
}
