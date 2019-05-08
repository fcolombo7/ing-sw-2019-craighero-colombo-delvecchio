package it.polimi.ingsw.network;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utils.Costants;
import it.polimi.ingsw.utils.Observable;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class SocketClientConnection extends Observable<String> implements ClientConnection,Runnable {
    private String nickname;

    private String motto;

    private boolean online;

    private Room room;

    private Socket socket;

    private Server server;

    private Scanner in;

    private PrintStream out;

    private HashMap<String, RequestManager> requestMap;

    public SocketClientConnection(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new Scanner(socket.getInputStream());
        out = new PrintStream(socket.getOutputStream());
        online=true;

        requestMap = new HashMap<>();
        loadRequests();
    }

    private void loadRequests() {
        requestMap.put(Costants.MSG_CLIENT_LOGIN, this::loginRequest);
        requestMap.put(Costants.MSG_CLIENT_CLOSE,this::closeRequest);
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    private void closeConnection() {
        try {
            out.println(Costants.MSG_SERVER_CLOSE);
            out.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        online = false;
    }

    @Override
    public void asyncAction(String message) {

    }

    @Override
    public void run() {
        try(Scanner in = new Scanner(socket.getInputStream())){
            while(true){
                String s=in.nextLine();
                requestMap.get(s).exec();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginRequest() {
        try{
            System.out.println("[Received a login request form socket]");
            String clientName = in.nextLine();
            String clientMotto = in.nextLine();
            String msg;
            if (server.checkClientLogin(clientName, this)) {
                msg = Costants.MSG_SERVER_POSITIVE_ANSWER;
            } else {
                msg = Costants.MSG_SERVER_NEGATIVE_ANSWER;
            }

            out.println(msg);
            out.flush();
            if (msg.equalsIgnoreCase(Costants.MSG_SERVER_POSITIVE_ANSWER)) {
                nickname=clientName;
                motto=clientMotto;
                server.joinAvailableRoom(this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void closeRequest() {
        closeConnection();
        server.deregisterConnection(this);
    }

    @FunctionalInterface
    private interface RequestManager {
        void exec();
    }
}
