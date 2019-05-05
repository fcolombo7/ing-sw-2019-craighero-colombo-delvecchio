package it.polimi.ingsw.network;

import it.polimi.ingsw.utils.Observable;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketClientConnection extends Observable<String> implements ClientConnection,Runnable {

    private Socket socket;

    private PrintStream out;

    private Server server;

    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private void close() {
        closeConnection();
        server.deregisterConnection(this);
    }

    public synchronized void closeConnection() {
        send("CLOSE_CONNECTION");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        active = false;
    }

    private void send(String message) {
        out.println(message);
        out.flush();
    }

    public void asyncAction(final String message){
        new Thread(()-> send(message)).start();
    }

    @Override
    public void run() {
        String name;
        try(Scanner in=new Scanner(socket.getInputStream()))
        {
            out = new PrintStream(socket.getOutputStream());
            String read = in.nextLine();
            name = read;
            if(server.firstLogin(name)) {
                server.addInWaitingRoom(this, name);
                while (isActive()) {
                    read = in.nextLine();
                    String msg=read;
                    //COMPOSE MESSAGE---SERIALIZATION
                    notify(msg);
                    if(msg.equalsIgnoreCase("close")) close();
                }
            }
        } catch (IOException | NoSuchElementException e) {
            e.printStackTrace();
        }finally{
            close();
        }
    }

}

