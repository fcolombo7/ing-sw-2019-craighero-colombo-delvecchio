package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.view.RemoteView;
import it.polimi.ingsw.network.view.View;
import it.polimi.ingsw.utils.Logger;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Room {

    /**
     * This attribute represents the minimum players allowed in the match
     */
    private static final int MIN_PLAYERS = Server.getMinPlayerNumber();

    /**
     * This attribute represents the maximum players allowed in the match
     */
    private static final int MAX_PLAYERS = Server.getMaxPlayerNumber();

    /**
     * This attribute represents the players in the room
     */
    private List<ClientConnection> players;

    /**
     * This attribute represents if the room is full
     */
    private boolean full=false;

    /**
     * This attribute represents if the room has already started a game
     */
    private boolean playing=false;

    /**
     * This attribute represents if the room is closed
     */
    private boolean closed=false;

    /**
     * This attribute represents if the room is refactorable
     */
    private boolean refactorable=false;

    /**
     * This attribute represents the room id
     */
    private int roomNumber;

    /**
     * This attribute represents the timer of the room
     */
    private Timer timer=null;

    /**
     * This attribute represents the waiting timers of the clients
     */
    private Map<String, Timer> waitingTimers =null;

    /**
     * This attribute represents the timers of the keepAlive calls
     */
    private Map<String, Timer> keepingAliveTimers =null;

    /**
     * This attribute represents the views of each client
     */
    private Map<String, View> views;

    /**
     * This attribute represents the controller linked to the room
     */
    private Controller controller;

    /**
     * This attribute represents the model linked to the room
     */
    private Game model;

    /**
     * This constructor create a room with the first client inside
     * @param client represents the first client entering thr room
     */
    public Room(ClientConnection client) {
        players = new ArrayList<>();
        players.add(client);
        controller=null;
        views=new HashMap<>();
        waitingTimers =new HashMap<>();
        keepingAliveTimers=new HashMap<>();
        client.firstInRoomAdvise();
        setUpKeepAlive(client);
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int number){ roomNumber=number;}

    /**
     * This method check if it is possible to join the room
     * @return a boolean representing if the room can be joined
     */
    public boolean canJoin(){
        return !(playing||full||closed);
    }

    public boolean isPlaying(){
        return  playing;
    }

    public List<ClientConnection> getPlayers() {
        return players;
    }

    /**
     * This method check if a join room request is possible and then add the client requesting
     * @param client represents the client requesting to join
     * @throws JoinRoomException when is not possible to join the room
     */
    public synchronized void joinRequest(ClientConnection client) throws JoinRoomException{
        if (!(playing||full)) {
            for (ClientConnection cc:players) {
                cc.joinRoomAdvise(client.getNickname());
                client.joinRoomAdvise(cc.getNickname());
            }
            players.add(client);
            setUpKeepAlive(client);
            Logger.logAndPrint(client.getNickname() + " has joined the room " + roomNumber);

            if (players.size() == MAX_PLAYERS) {
                full = true;
                startCountDown(Server.getKeepAliveTimer());
            } else if (players.size() == MIN_PLAYERS) {
                startCountDown(Server.getWaitingRoomTimer());
            }
        } else {
            throw new JoinRoomException("full");
        }
    }

    /**
     * This method remove a client form the room
     * @param client represents the client to be removed
     * @return if the room is empty
     */
    public synchronized boolean remove(ClientConnection client){
        if(!players.contains(client)) throw new IllegalStateException("Removing player which not exits");
        //REMOVING TIMERS
        resetClientTimers(client);
        players.remove(client);
        for (ClientConnection cc : players) {
            cc.exitRoomAdvise(client.getNickname());
        }

        Logger.logAndPrint(client.getNickname() + " has left the room " + roomNumber);
        if (players.size() < Server.getMinPlayerNumber())
            resetCountDown();
        return players.isEmpty();
    }

    /**
     * This method starts the countdown for the room closing
     * @param time represents the timer length to start
     */
    private void startCountDown(long time) {
        resetCountDown();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startMatch();
            }
        }, time * 1000);
    }

    /**
     * This method reset countdown
     */
    private void resetCountDown() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer=null;
        }
    }

    public Controller getController() {
        if(!playing)throw  new IllegalStateException("The match is not started.");
        return controller;
    }

    /**
     * This methods sets the timer for the keepAlive method
     * @param client represents the connection to set
     */
    private void setUpKeepAlive(ClientConnection client) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                keepClientAlive(client);
            }
        }, Server.getKeepAliveTimer()*1000, Server.getKeepAliveFrequency() * 1000);
        keepingAliveTimers.put(client.getNickname(), t);
    }

    /**
     * This method keep the client alive and check if it is possible
     * @param client represents the client to check
     */
    private void keepClientAlive(ClientConnection client){
        Timer t=new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Logger.logErr("KEEP ALIVE: TIMER SCADUTO PER "+client.getNickname());
                if(playing)
                    getController().cancelTimerAfterKeepAlive(client.getNickname());
                handleDisconnection(client);
            }
        },Server.getKeepAliveTimer()*1000);
        waitingTimers.put(client.getNickname(),t);
        client.keepAlive();
    }

    /**
     * This method let the client to be alive
     * @param client represents the client alive
     */
    public synchronized void isAlive(ClientConnection client){
        //Logger.logAndPrint("["+client.getNickname()+" IS ALIVE]");
        if(waitingTimers !=null){
            Timer t= waitingTimers.get(client.getNickname());
            if(t!=null){
                t.cancel();
                t.purge();
                waitingTimers.remove(client.getNickname());
            }
        }
    }

    /**
     * This method handle the disconnection of a client
     * @param client represents the client disconnecting
     */
    private void handleDisconnection(ClientConnection client) {
        resetClientTimers(client);
        if(playing) {
            controller.addDisconnected(client.getNickname());
            model.deregister(views.get(client.getNickname()));
            views.remove(client.getNickname());
        }
        client.notifyDisconnetion();
        client.closeConnection();
    }

    /**
     * This method force a client diconnection
     * @param nickname represents the nickname of the client to disconnect
     */
    public void forceDisconnection(String nickname) {
        for(ClientConnection cc: players){
            if(cc.getNickname().equals(nickname)) {
                handleDisconnection(cc);
                return;
            }
        }
    }

    /**
     * This method reset the timer of the client
     * @param client represents the client resetting
     */
    private void resetClientTimers(ClientConnection client){
        Timer t= keepingAliveTimers.get(client.getNickname());
        if(t!=null){
            t.cancel();
            t.purge();
            keepingAliveTimers.remove(client.getNickname());
        }
        Timer t2= waitingTimers.get(client.getNickname());
        if(t2!=null){
            t2.cancel();
            t2.purge();
            waitingTimers.remove(client.getNickname());
        }
    }

    /**
     * This method sets up and create a new match
     */
    private void startMatch() {
        model= new Game();
        controller= new Controller(model,this);
        boolean first=true;
        for (ClientConnection client:players) {
            boolean val=false;
            if(first) {
                val = first;
                first = false;
            }
            Player p=new Player(client.getNickname(),client.getMotto(),val);
            model.addPlayer(p);
            View pView=new RemoteView(p,client);
            model.register(pView);
            views.put(p.getNickname(),pView);
        }

        playing=true;
        controller.start();
    }

    /**
     * This method recovers a client disconnected
     * @param client represents the client recovering
     */
    public void recoverClient(ClientConnection client) {
        if(controller.isDisconnected(client.getNickname())){
            Player p=controller.getPlayerToRecover(client.getNickname());
            Logger.logAndPrint(client.getNickname() + " has been recovered in the room " + roomNumber);
            View pView=new RemoteView(p,client);
            model.register(pView);
            views.put(p.getNickname(),pView);
            controller.recoverPlayer(p);
            /*
            ExecutorService ex= Executors.newSingleThreadExecutor();
            ex.submit(()->{
                for (ClientConnection cc:players) {
                    if(!cc.getNickname().equals(client.getNickname())) {
                        cc.recoverAdvise(client.getNickname());
                    }
                }
            });
             */
            setUpKeepAlive(client);
        }else throw new IllegalStateException("Player "+client.getNickname()+" is not disconnected.");
    }


    /**
     * This method stops the timers of a client
     * @param sender represents the nickname of the client to stop
     */
    public void stopTimers(String sender) {
        for(ClientConnection cc:players){
            if(cc.getNickname().equals(sender)) {
                resetClientTimers(cc);
                return;
            }
        }
    }

    /**
     * This method close the room
     */
    public void close() {
        for(ClientConnection cc:players){
            resetClientTimers(cc);
        }
        full=true;
        playing=false;
        closed = true;
        Logger.logAndPrint("ROOM "+roomNumber+" IS CLOSED");
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean canRefactor() {
        return refactorable;
    }

    /**
     * This method clear the player of the room
     */
    public void clearPlayers(){
        players.clear();
    }
}