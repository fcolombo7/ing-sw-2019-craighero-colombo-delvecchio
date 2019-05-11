package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.model.messages.matchanswer.MatchAnswer;
import it.polimi.ingsw.model.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.network.controller.Room;
import it.polimi.ingsw.utils.Costants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.Observable;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class SocketClientConnection extends Observable<MatchAnswer> implements ClientConnection,Runnable {
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
        online=false;

        requestMap = new HashMap<>();
        loadRequests();
    }

    private void loadRequests() {
        requestMap.put(Costants.MSG_CLIENT_LOGIN, this::loginRequest);
        requestMap.put(Costants.MSG_CLIENT_CLOSE,this::closeRequest);
        requestMap.put(Costants.MSG_CLIENT_ANSWER,this::onAnswerRequest);
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

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }

    private void closeConnection() {
        try {
            out.println(Costants.MSG_SERVER_CLOSE);
            out.flush();
            socket.close();
        } catch (IOException e) {
            Logger.logErr(e.getMessage());
        }
        online = false;
    }

    @Override
    public void sendMatchMessage(MatchMessage message) {
        Gson gson = new Gson();
        Logger.log("[Socket] MatchMessage sending to "+nickname+":");
        Logger.log(gson.toJson(message));
        out.println(gson.toJson(message));
    }

    @Override
    public void run() {
       while(true){
           String s=in.nextLine();
           requestMap.get(s).exec();
       }
    }

    private void loginRequest() {
        Logger.log("[Received a login request from socket]");
        if(online){
            out.println(Costants.MSG_SERVER_ALREADY_LOGGED);
            out.flush();
        }
        String clientName = in.nextLine();
        String clientMotto = in.nextLine();
        String msg;
        if (server.checkClientLogin(clientName, this)) {
            msg = Costants.MSG_SERVER_POSITIVE_ANSWER;
            online=true;
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
    }

    private void closeRequest() {
        closeConnection();
        server.deregisterConnection(this);
    }

    private void onAnswerRequest(){
        Logger.log("[Received an answer request from socket]");
        Gson gson=new Gson();
        String msg=in.nextLine();
        Logger.log("[JSON answer] "+msg);
        MatchAnswer matchAnswer=gson.fromJson(msg,MatchAnswer.class);
        notify(matchAnswer);
    }

    @FunctionalInterface
    private interface RequestManager {
        void exec();
    }
}
