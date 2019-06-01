package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.controller.JoinRoomException;
import it.polimi.ingsw.network.controller.Room;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server{

    private SocketServer socketServer;

    private RMIServer rmiServer;

    private List<Room> rooms;

    private HashMap<String, ClientConnection> players;

    public Server() {
        players=new HashMap<>();
        rooms=new ArrayList<>();
        rmiServer=new RMIServer(this);
        socketServer=new SocketServer(this);
    }

    public void startServer() throws ServerException {
        rmiServer.start(Constants.RMI_PORT);
        socketServer.startServer(Constants.SOCKET_PORT);
    }


    public synchronized boolean checkClientLogin(String nickname, ClientConnection client){
        Logger.log("Login request received from " + nickname);
        if(nickname.length()>0&&!players.containsKey(nickname)){
            players.put(nickname, client);
            Logger.log(nickname+" is in.");
            return true;
        }else{
            Logger.log(nickname+" is already in.");
            return false;
        }
    }

    public synchronized void deregisterConnection(ClientConnection client){
        Logger.log("Deregistering "+client.getNickname()+" from server");
        players.remove(client.getNickname());
        Room room=client.getRoom();
        if(room.remove(client)){
            for(Room r:rooms){
                if(r.getRoomNumber()>room.getRoomNumber()) {
                    r.setRoomNumber(r.getRoomNumber()-1);
                }
            }
            rooms.remove(room);
        }
    }

    public synchronized void joinAvailableRoom(ClientConnection client){
        try {
            joinRoom(client);
        } catch (JoinRoomException e) {
            addNewRoom(client);
            Logger.log("New room has been created");
        }
    }

    private void joinRoom(ClientConnection client) throws JoinRoomException {
        if(rooms.isEmpty()) throw  new JoinRoomException("No room created");
        Room lastRoom=rooms.get(rooms.size() - 1);
        for(int i=0;i<rooms.size();i++){
            if(rooms.get(i).canJoin())
                lastRoom = rooms.get(i);
        }
        lastRoom.joinRequest(client);
        client.setRoom(lastRoom);
    }

    private synchronized void addNewRoom(ClientConnection client) {
        Room room = new Room(client);
        rooms.add(room);
        room.setRoomNumber(rooms.size()-1);
        client.setRoom(room);
    }

    public ClientConnection getClientConnection(String nickname){
        for (String key:players.keySet()) {
            if(key.equalsIgnoreCase(nickname)){
                return players.get(nickname);
            }
        }
        throw new IllegalArgumentException("No player connected with nickname "+nickname);
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.startServer();

            Logger.log(Constants.RMI_SERVER_NAME + " started:");
            Logger.log("(RMI: " + Constants.RMI_PORT + ", socket: " + Constants.SOCKET_PORT + ")");
        } catch (ServerException e) {
            Logger.logErr(e.getMessage());
        }
    }

}
