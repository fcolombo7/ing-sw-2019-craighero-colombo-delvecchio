package it.polimi.ingsw.network.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.network.view.RemoteView;
import it.polimi.ingsw.network.view.View;
import it.polimi.ingsw.utils.Costants;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Room {

    private static final int ROOM_WAITING_TIME = Costants.WAITING_ROOM_TIMER;

    private static final int MIN_PLAYERS = Costants.ROOM_MIN_PLAYERS;

    private static final int MAX_PLAYERS = Costants.ROOM_MAX_PLAYERS;

    private List<ClientConnection> players;

    private boolean full;

    private boolean playing;

    private int roomNumber;

    private Timer timer;

    public Room(ClientConnection client) {

        players = new ArrayList<>();
        players.add(client);

        full = false;
        playing=false;
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
            players.add(client);

            String message = client.getNickname() + " has joined the room " + roomNumber;
            //avvisare i giocatori?
            Logger.log(message);

            if (players.size() == MAX_PLAYERS) {
                full = true;
                //resetCountDown();
                //startCountDown(0);
                startMatch();
            } else if (players.size() == MIN_PLAYERS) {
                //startCountDownTimer(ROOM_WAITING_TIME);
            }
        } else {
            throw new JoinRoomException("full");
        }
    }

    public synchronized boolean remove(ClientConnection client){
        if(!players.contains(client)) throw new IllegalArgumentException("Client "+client.getNickname()+" is not in room "+roomNumber);
        players.remove(client);
        String message = client.getNickname() + " has left the room " + roomNumber;
        //logToPlayer(player, message);
        Logger.log(message);
        if(players.size()<Costants.ROOM_MIN_PLAYERS)
            stopCountdown();
        return players.isEmpty();
    }

    private void stopCountdown() {
    }

    /*
    private void startCountDown(int time) {
        logToAllPlayers("Time to start the game: "+ time + "sec");

        timer = new Timer();
        timer.scheduleAtFixedRate(new RoomCountDownHandler(time, new RoomGameHandler(), null), 1000, 1000);
    }

    private void resetCountDown() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
 */

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
