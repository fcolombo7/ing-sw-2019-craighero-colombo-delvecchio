package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.messages.LoginMessage;
import it.polimi.ingsw.model.messages.matchanswer.BoardPreferenceAnswer;
import it.polimi.ingsw.model.messages.matchanswer.MatchAnswer;
import it.polimi.ingsw.model.messages.matchmessages.BoardCreationMessage;
import it.polimi.ingsw.model.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.utils.Costants;
import it.polimi.ingsw.utils.Logger;

import java.io.*;
import java.net.Socket;
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

    public TestClientSocket(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        socket = new Socket(ip, port);
        System.out.println("Connection established");
        socketIn = new Scanner(socket.getInputStream());
        socketOut = new PrintWriter(socket.getOutputStream());
        stdin = new Scanner(System.in);
    }

    public void startClient() {
        login();
        Runnable receiver= () -> {
            try {
                Gson gson=new Gson();
                while (true) {
                    String socketLine = socketIn.nextLine();
                    System.out.println("[SERVER] "+socketLine);
                    if (aBoolean) {
                        try{
                            BoardCreationMessage msg=gson.fromJson(socketLine, BoardCreationMessage.class);
                            GameBoard board=msg.getGameBoard();
                        }catch (Exception e){
                            Logger.log("Tried but failed to get a BoardPreferenceAnswer from the received Json String.");
                        }
                    }

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
                    if(msg.equalsIgnoreCase("SendAnswerRequest"))aBoolean=true;
                    System.out.println(msg);
                    socketOut.println(msg);
                    socketOut.flush();
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

    private void login() {
        Gson gson= new Gson();
        String msg= Costants.MSG_CLIENT_LOGIN;
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
    }


    public static void main(String[] args) {
        try {
            TestClientSocket client = new TestClientSocket("127.0.0.1", 12345);
            client.startClient();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
