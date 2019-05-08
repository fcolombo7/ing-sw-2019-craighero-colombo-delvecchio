package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.ClientConnection;
import it.polimi.ingsw.network.JoinRoomException;
import it.polimi.ingsw.network.Room;
import it.polimi.ingsw.utils.Costants;

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
        rmiServer.start(Costants.RMI_PORT);
        socketServer.startServer(Costants.SOCKET_PORT);
    }


    public synchronized boolean checkClientLogin(String nickname, ClientConnection client){
        System.out.println("Login request received from "+nickname);
        if(nickname.length()>0&&!players.containsKey(nickname)){
            players.put(nickname, client);
            System.out.println(nickname+" is in.");
            return true;
        }else{
            System.out.println(nickname+" is already in.");
            return false;
        }
    }

    public synchronized void deregisterConnection(ClientConnection client){
        System.out.println("Logout request received from "+client.getNickname());
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
            System.out.println("New room has been created");
        }
    }

    private void joinRoom(ClientConnection client) throws JoinRoomException {
        if(rooms.isEmpty()) throw  new JoinRoomException("No room created");
        Room lastRoom = rooms.get(rooms.size() - 1);
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

            System.out.print(Costants.RMI_SERVER_NAME + " started: \n");
            System.out.println("(RMI: " + Costants.RMI_PORT + ", socket: " + Costants.SOCKET_PORT + ")");
        } catch (ServerException e) {
            e.printStackTrace();
        }
    }

}
