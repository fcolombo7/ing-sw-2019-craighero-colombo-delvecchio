package it.polimi.ingsw.network.tests;

import com.google.gson.Gson;
import it.polimi.ingsw.network.controller.messages.LoginMessage;
import it.polimi.ingsw.network.controller.messages.room.ExitMessage;
import it.polimi.ingsw.network.controller.messages.room.FirstInMessage;
import it.polimi.ingsw.network.controller.messages.room.JoinMessage;
import it.polimi.ingsw.utils.Constants;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestClientSocket {
    private String ip;
    private int port;
    Scanner socketIn;
    PrintWriter socketOut;
    Scanner stdin;
    Socket socket;
    boolean aBoolean=false;
    private ExecutorService executor = Executors.newFixedThreadPool(2);
    private HashMap<String,handler> map;

    public TestClientSocket(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        socket = new Socket(ip, port);
        System.out.println("Connection established");
        socketIn = new Scanner(socket.getInputStream());
        socketOut = new PrintWriter(socket.getOutputStream());
        stdin = new Scanner(System.in);
        map=new HashMap<>();
        map.put(Constants.PLAYER_JOIN,()->{
            Gson gson= new Gson();
            String socketLine=socketIn.nextLine();
            JoinMessage msg=gson.fromJson(socketLine,JoinMessage.class);
            System.out.println("User "+msg.getPlayer()+" as joined the room...");
        });

        map.put(Constants.PLAYER_EXIT,()->{
            Gson gson= new Gson();
            String socketLine=socketIn.nextLine();
            ExitMessage msg=gson.fromJson(socketLine,ExitMessage.class);
            System.out.println("User "+msg.getPlayer()+" as left the room...");
        });

        map.put(Constants.FIRST_PLAYER,()->{
            System.out.println("You are the first player in the room...");
        });

        map.put(Constants.CREATION_MESSAGE,()->{
            System.out.println("MATCH HAS BEEN CREATED");
            System.out.print("BOARD: ");
        });
    }

    public void startClient() {
        login();
        Runnable receiver= () -> {
            try {
                Gson gson=new Gson();
                while (true) {
                    String socketLine = socketIn.nextLine();
                    System.out.println("[SERVER] "+socketLine);
                    map.get(socketLine).exec();
                }

            }catch(NoSuchElementException e) {
                System.out.println("Connection closed");
            } finally {
                stdin.close();
                socketIn.close();
                socketOut.close();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable sender= () -> {
            try {
                while (true) {
                    String msg = stdin.nextLine();
                    System.out.println(msg);
                    socketOut.println(msg);
                    socketOut.flush();
                    if(msg.equalsIgnoreCase("BOARD PREFERENCE"))
                    {socketOut.println("{\"boardReference\":1,\"sender\":\"FIL\",\"answer\":\"BOARD PREFERENCE\"}");
                    socketOut.flush();}
                }
            }catch(NoSuchElementException e) {
                System.out.println("Connection closed");
            } finally {
                stdin.close();
                socketIn.close();
                socketOut.close();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        executor.submit(receiver);
        executor.submit(sender);
        System.out.println("[LOG] executor started");
    }

    private void checkMessage(String socketLine) {
        Gson gson=new Gson();
        try{
            JoinMessage msg=gson.fromJson(socketLine,JoinMessage.class);
            if(msg.getType().equalsIgnoreCase(Constants.PLAYER_JOIN)){
                System.out.println("User "+msg.getPlayer()+" as joined the room...");
                return;
            }
        }catch(Exception e){
            System.out.println("[NOT JOIN MESSAGE]");
        }
        try{
            ExitMessage msg=gson.fromJson(socketLine,ExitMessage.class);
            if(msg.getType().equalsIgnoreCase(Constants.PLAYER_EXIT)){
                System.out.println("User "+msg.getPlayer()+" as left the room...");
                return;
            }
        }catch(Exception e){
            System.out.println("[NOT EXIT MESSAGE]");
        }
        try{
            FirstInMessage msg=gson.fromJson(socketLine,FirstInMessage.class);
            if(msg.getType().equalsIgnoreCase(Constants.PLAYER_EXIT)){
                System.out.println("You are the first in room!");
                return;
            }
        }catch(Exception e){
            System.out.println("[NOT FIRST IN MESSAGE]");
        }

    }

    private void login() {
        Gson gson= new Gson();
        String msg= Constants.MSG_CLIENT_LOGIN;
        socketOut.println(msg);
        socketOut.flush();

        String nick, motto;
        LoginMessage loginMessage;
        System.out.print("nickname: ");
        nick=stdin.nextLine();
        System.out.print("motto: ");
        motto=stdin.nextLine();

        loginMessage=new LoginMessage(nick,motto);
        socketOut.println(gson.toJson(loginMessage));
        socketOut.flush();
        System.out.println(socketIn.nextLine());
    }


    public static void main(String[] args) {
        try {
            TestClientSocket client = new TestClientSocket("127.0.0.1", 12345);
            client.startClient();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FunctionalInterface
    private interface handler{
        void exec();
    }

}
