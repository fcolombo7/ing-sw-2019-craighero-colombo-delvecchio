package it.polimi.ingsw.network;

import it.polimi.ingsw.utils.Costants;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Room {

    private static final int ROOM_WAITING_TIME = Costants.WAITING_ROOM_TIMER;

    private static final int minPlayers = Costants.ROOM_MIN_PLAYERS;

    private static final int maxPlayers = Costants.ROOM_MAX_PLAYERS;

    private static int roomsSize=0;

    private List<ClientConnection> players;

    private boolean full;

    private int roomNumber;

    private Timer timer;


    public Room(ClientConnection client) {

        roomNumber=roomsSize;
        roomsSize++;

        players = new ArrayList<>();
        players.add(client);

        full = false;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean canJoin(){
        return !full;
    }

    public List<ClientConnection> getPlayers() {
        return players;
    }

    public synchronized void joinRequest(ClientConnection client) throws JoinRoomException{
        if (!full) {
            players.add(client);

            String message = client.getNickname() + "has joined to room " + roomNumber;
            //logToPlayer(player, message);
            System.out.println(message);

            if (players.size() == maxPlayers) {
                full = true;
                //resetCountDown();
                //startCountDown(0);
            } else if (players.size() == minPlayers) {
                //startCountDownTimer(ROOM_WAITING_TIME);
            }
        } else {
            throw new JoinRoomException("full");
        }
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

}
