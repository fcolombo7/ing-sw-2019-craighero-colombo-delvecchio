package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.room.ExitMessage;
import it.polimi.ingsw.network.controller.messages.room.FirstInMessage;
import it.polimi.ingsw.network.controller.messages.room.JoinMessage;
import it.polimi.ingsw.network.controller.messages.room.PingMessage;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.network.view.RemoteView;
import it.polimi.ingsw.network.view.View;
import it.polimi.ingsw.utils.Costants;
import it.polimi.ingsw.utils.Logger;

import java.util.*;

public class Room {

    private static final long ROOM_WAITING_TIME = Costants.WAITING_ROOM_TIMER;

    private static final long ROOM_WAITING_PONG = Costants.WAITING_ROOM_PONG;

    private static final int MIN_PLAYERS = Costants.ROOM_MIN_PLAYERS;

    private static final int MAX_PLAYERS = Costants.ROOM_MAX_PLAYERS;

    private List<ClientConnection> players;

    private boolean full=false;

    private boolean playing=false;

    private int roomNumber;

    private Timer timer=null;

    private Map<String, Timer> clientCheck=null;

    public Room(ClientConnection client) {
        players = new ArrayList<>();
        players.add(client);
        client.sendRoomMessage(new FirstInMessage());
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
                cc.sendRoomMessage(new JoinMessage(client.getNickname()));
            }
            players.add(client);
            Logger.log(client.getNickname() + " has joined the room " + roomNumber);

            if (players.size() == MAX_PLAYERS) {
                full = true;
                startCountDown(ROOM_WAITING_PONG);
            } else if (players.size() == MIN_PLAYERS) {
                startCountDown(ROOM_WAITING_TIME);
            }
        } else {
            throw new JoinRoomException("full");
        }
    }

    public synchronized boolean remove(ClientConnection client){
        if(!players.contains(client)) throw new IllegalArgumentException("Client "+client.getNickname()+" is not in room "+roomNumber);
        players.remove(client);
        for (ClientConnection cc:players) {
            cc.sendRoomMessage(new ExitMessage(client.getNickname()));
        }

        Logger.log(client.getNickname() + " has left the room " + roomNumber);
        if(players.size()<Costants.ROOM_MIN_PLAYERS)
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

    private void checkConnection(){
        resetCheck();
        clientCheck=new HashMap<>();
        for(ClientConnection cc:players){
            cc.sendRoomMessage(new PingMessage());
            Timer ccTimer=new Timer();
            ccTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    cc.closeConnection();
                }
            },0);

            clientCheck.put(cc.getNickname(),ccTimer);
        }
    }

    private void resetCheck() {
        for (String key:clientCheck.keySet()) {
            Timer t=clientCheck.get(key);
            if(t!=null){
                t.cancel();
                t.purge();
                clientCheck.remove(key);
            }
        }
        clientCheck=null;
    }

    public synchronized void onPongReceived(ClientConnection client){
        if(clientCheck!=null){
            Timer t=clientCheck.get(client.getNickname());
            if(t!=null){
                t.cancel();
                t.purge();
                clientCheck.remove(client.getNickname());
            }
        }
    }

    private void startMatch() {
        Game model= new Game();
        Controller controller= new Controller(model);
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
            pView.register(controller);
        }
        playing=true;
        controller.start();
    }

}
