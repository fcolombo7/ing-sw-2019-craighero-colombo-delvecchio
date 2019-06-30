package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.view.RemoteView;
import it.polimi.ingsw.network.view.View;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.util.*;

public class Room {

    private static final int MIN_PLAYERS = Server.getMinPlayerNumber();

    private static final int MAX_PLAYERS = Server.getMaxPlayerNumber();

    private List<ClientConnection> players;

    private boolean full=false;

    private boolean playing=false;

    private int roomNumber;

    private Timer timer=null;

    private Map<String, Timer> waitingTimers =null;

    private Map<String, Timer> keepingAliveTimers =null;

    private Map<String, View> views;

    private Controller controller;

    private Game model;

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

    public boolean canJoin(){
        return !(playing||full);
    }

    public boolean isPlaying(){
        return  playing;
    }

    public List<ClientConnection> getPlayers() {
        return players;
    }

    public synchronized void joinRequest(ClientConnection client) throws JoinRoomException{
        if (!(playing||full)) {
            for (ClientConnection cc:players) {
                cc.joinRoomAdvise(client.getNickname());
                client.joinRoomAdvise(cc.getNickname());
            }
            players.add(client);
            setUpKeepAlive(client);
            Logger.logServer(client.getNickname() + " has joined the room " + roomNumber);

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

    public synchronized boolean remove(ClientConnection client){
        if(!players.contains(client)) throw new IllegalStateException("Removing player which not exits");
        //REMOVING TIMERS
        resetClientTimers(client);
        players.remove(client);
        for (ClientConnection cc : players) {
            cc.exitRoomAdvise(client.getNickname());
        }

        Logger.logServer(client.getNickname() + " has left the room " + roomNumber);
        if (players.size() < Server.getMinPlayerNumber())
            resetCountDown();
        return players.isEmpty();
    }

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

    private void keepClientAlive(ClientConnection client){
        Timer t=new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Logger.logErr("KEEP ALIVE: TIMER SCADUTO PER "+client.getNickname());
                handleDisconnection(client);
            }
        },Server.getKeepAliveTimer()*1000);
        waitingTimers.put(client.getNickname(),t);
        client.keepAlive();
    }

    public synchronized void isAlive(ClientConnection client){
        //Logger.logServer("["+client.getNickname()+" IS ALIVE]");
        if(waitingTimers !=null){
            Timer t= waitingTimers.get(client.getNickname());
            if(t!=null){
                t.cancel();
                t.purge();
                waitingTimers.remove(client.getNickname());
            }
        }
    }

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

    public void forceDisconnection(String nickname) {
        for(ClientConnection cc: players){
            if(cc.getNickname().equals(nickname)) {
                handleDisconnection(cc);
                return;
            }
        }
    }

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

    public void recoverClient(ClientConnection client) {
        if(controller.isDisconnected(client.getNickname())){
            Player p=controller.getPlayerToRecover(client.getNickname());
            Logger.logServer(client.getNickname() + " has been recovered in the room " + roomNumber);
            View pView=new RemoteView(p,client);
            model.register(pView);
            views.put(p.getNickname(),pView);
            controller.recoverPlayer(p);
            for (ClientConnection cc:players) {
                if(!cc.getNickname().equals(client.getNickname())) {
                    cc.recoverAdvise(client.getNickname());
                }
            }
            setUpKeepAlive(client);
        }else throw new IllegalStateException("Player "+client.getNickname()+" is not disconnected.");
    }

    public void stopTimers(String sender) {
        for(ClientConnection cc:players){
            if(cc.getNickname().equals(sender)) {
                resetClientTimers(cc);
                return;
            }
        }
    }

    public void close() {
        for(ClientConnection cc:players){
            resetClientTimers(cc);
        }
        full=true;
        playing=false;
        List<ClientConnection> clientConnections=new ArrayList<>(players);
        players.clear();
        for(ClientConnection cc:clientConnections) cc.closeConnection();
    }
}