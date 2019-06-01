package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.network.view.RemoteView;
import it.polimi.ingsw.network.view.View;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.util.*;

public class Room {

    private static final int MIN_PLAYERS = Constants.ROOM_MIN_PLAYERS;

    private static final int MAX_PLAYERS = Constants.ROOM_MAX_PLAYERS;

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
            Logger.log(client.getNickname() + " has joined the room " + roomNumber);

            if (players.size() == MAX_PLAYERS) {
                full = true;
                startCountDown(Constants.KEEP_ALIVE_WAITING_TIME);
                startMatch();
            } else if (players.size() == MIN_PLAYERS) {
                startCountDown(Constants.WAITING_ROOM_TIMER);
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

        Logger.log(client.getNickname() + " has left the room " + roomNumber);
        if (players.size() < Constants.ROOM_MIN_PLAYERS)
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
        }, Constants.KEEP_ALIVE_WAITING_TIME*1000, Constants.KEEP_ALIVE_FREQUENCY * 1000);
        keepingAliveTimers.put(client.getNickname(), t);
    }

    private void resetKeepAlive() {
        for(Timer t:waitingTimers.values()) {
            if(t!=null){
                t.cancel();
                t.purge();
            }
        }
        for(Timer t:keepingAliveTimers.values()){
            if(t!=null){
                t.cancel();
                t.purge();
            }
        }
        waitingTimers.clear();
        keepingAliveTimers.clear();
    }

    private void keepClientAlive(ClientConnection client){
        Timer t=new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Logger.logErr("TIMER SCADUTO PER "+client.getNickname());
                handleDisconnection(client);
            }
        },Constants.KEEP_ALIVE_WAITING_TIME*1000);
        waitingTimers.put(client.getNickname(),t);
        client.keepAlive();
    }

    public synchronized void isAlive(ClientConnection client){
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
        client.closeConnection();
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
        controller= new Controller(model);
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

}
