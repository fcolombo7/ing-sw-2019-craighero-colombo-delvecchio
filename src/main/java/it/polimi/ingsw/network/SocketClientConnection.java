package it.polimi.ingsw.network;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utils.Costants;
import it.polimi.ingsw.utils.Observable;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class SocketClientConnection extends Observable<String> implements ClientConnection,Runnable {
    private String nickname;

    private String motto;

    private boolean online;

    private Room room;

    private Socket socket;

    private Server server;

    private ObjectInputStream inputStream;

    private ObjectOutputStream outputStream;

    private HashMap<Object, RequestManager> requestMap;

    public SocketClientConnection(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        outputStream=new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        inputStream=new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        online=true;

        requestMap = new HashMap<>();
        loadRequests();
    }

    private void loadRequests() {
        requestMap.put(Costants.MSG_CLIENT_LOGIN, this::loginRequest);
        requestMap.put(Costants.MSG_CLIENT_CLOSE,this::closeRequest);
    }

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
            outputStream.writeObject(Costants.MSG_SERVER_CLOSE);
            outputStream.flush();
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
        try{
            while(true){
                Object object = inputStream.readObject();
                requestMap.get(object).exec();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loginRequest() {
        try {
            String clientName = (String) inputStream.readObject();
            String clientMotto = (String) inputStream.readObject();
            String msg;
            if (server.checkClientLogin(clientName, this)) {
                msg = Costants.MSG_SERVER_POSITIVE_ANSWER;
            } else {
                msg = Costants.MSG_SERVER_NEGATIVE_ANSWER;
            }

            outputStream.writeObject(msg);
            outputStream.flush();
            if (msg.equalsIgnoreCase(Costants.MSG_SERVER_POSITIVE_ANSWER)) {
                nickname=clientName;
                motto=clientMotto;
                server.joinAvailableRoom(this);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeRequest() {
        closeConnection();
        //server.deregisterConnection(this);
    }



    @FunctionalInterface
    private interface RequestManager {

        void exec();
    }
}
