package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.model.messages.LoginMessage;
import it.polimi.ingsw.model.messages.matchanswer.BoardPreferenceAnswer;
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
        try {
            Logger.log(gson.toJson(message));
            out.println(gson.toJson(message));
        }catch(Exception e){
            Logger.logErr(e.getMessage());
        }
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
            Logger.log("Invalid request: client already logged in");
        }
        Gson gson=new Gson();
        String msg=in.nextLine();
        try{
            LoginMessage loginMessage=gson.fromJson(msg, LoginMessage.class);
            String cNickname = loginMessage.getNickname();
            String cMotto = loginMessage.getMotto();
            if (server.checkClientLogin(cNickname, this)) {
                msg = Costants.MSG_SERVER_POSITIVE_ANSWER;
                online=true;
            } else {
                msg = Costants.MSG_SERVER_NEGATIVE_ANSWER;
            }

            out.println(msg);
            out.flush();
            if (msg.equalsIgnoreCase(Costants.MSG_SERVER_POSITIVE_ANSWER)) {
                this.nickname=cNickname;
                this.motto=cMotto;
                server.joinAvailableRoom(this);
            }
        }catch (Exception e){
            Logger.logErr("Cannot get a correct LoginMessage from the received Json string.");
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

        //foreach class which extends the MatchAnswer one. [the class parameters of 'gson.fromJson' method must be the class of the dynamic type]
        try{
            MatchAnswer matchAnswer=gson.fromJson(msg, BoardPreferenceAnswer.class);
            notify(matchAnswer);
            return;
        }catch (Exception e){
            Logger.log("Tried but failed to get a BoardPreferenceAnswer from the received Json String.");
        }

        Logger.logErr("Cannot get a correct MatchAnswer from the received Json string.");
    }

    @FunctionalInterface
    private interface RequestManager {
        void exec();
    }
}
