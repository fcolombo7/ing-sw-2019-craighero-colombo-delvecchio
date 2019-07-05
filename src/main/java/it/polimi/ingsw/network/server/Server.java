package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.controller.JoinRoomException;
import it.polimi.ingsw.network.controller.Room;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents the server of the game
 */
public class Server{

    /* CONFIG PARAMETERS */
    private static String rmiServerName=Constants.RMI_SERVER_NAME;
    private static int rmiServerPort=Constants.RMI_PORT;
    private static int socketServerPort=Constants.SOCKET_PORT;
    private static int minPlayerNumber=Constants.ROOM_MIN_PLAYERS;
    private static int maxPlayerNumber=Constants.ROOM_MAX_PLAYERS;
    private static int skullNumber=Constants.DEFAULT_SKULL;
    private static long waitingRoomTimer=Constants.WAITING_ROOM_TIMER;
    private static long keepAliveFrequency=Constants.KEEP_ALIVE_FREQUENCY;
    private static long keepAliveTimer=Constants.KEEP_ALIVE_WAITING_TIME;
    private static long turnTimer=Constants.TURN_TIMER;
    private static long quickMoveTimer=Constants.QUICK_MOVE_TIMER;


    /*OTHER ATTRIBUTES*/
    private SocketServer socketServer;

    private RMIServer rmiServer;

    private List<Room> rooms;

    private HashMap<String, ClientConnection> players;

    private HashMap<String, Integer> disconnected;

    /**
     * This constructor initialize properly the server
     * @param hostname represents the identifier of the host
     */
    private Server(String hostname) {
        players=new HashMap<>();
        disconnected=new HashMap<>();
        rooms=new ArrayList<>();
        rmiServer=new RMIServer(this,hostname);
        socketServer=new SocketServer(this);
    }

    /**
     * This method starts RMI and socket type servers
     * @throws ServerException when a server error occurs
     */
    private void startServer() throws ServerException {
        rmiServer.start(Server.getRmiServerPort());
        socketServer.startServer(Server.getSocketServerPort());
    }

    /**
     * This method check if the client logs properly and create a connection with it
     * @param nickname represents the client nickname
     * @param client represents the type of the connection of the client
     * @return an integer representing the action made by the server
     */
    synchronized int checkClientLogin(String nickname, ClientConnection client){
        roomRefactor();
        Logger.logAndPrint("Login request received from " + nickname);
        if(nickname.length()>0&&!players.containsKey(nickname)){
            if(!disconnected.containsKey(nickname)){
                players.put(nickname, client);
                Logger.logAndPrint(nickname+" is in.");
                return 1;
            }else {
                Room room=rooms.get(disconnected.get(nickname));
                players.put(nickname, client);
                client.setRoom(room);
                Logger.logAndPrint(nickname+" is back in room "+ room.getRoomNumber()+".");
                return 2;
            }
        }else{
            Logger.logAndPrint(nickname+" is already in.");
            return 0;
        }
    }

    /**
     * This method clear the room and disconnects its player
     */
    private void roomRefactor() {
        List<Room> tRooms=new ArrayList<>(rooms);
        for (Room r:tRooms) {
            if(r.isClosed()){
                List<ClientConnection> temp=new ArrayList<>(r.getPlayers());
                for (ClientConnection cc:temp)
                    deregisterConnection(cc);
                List<String> keySet=new ArrayList<>(disconnected.keySet());
                for(String key:keySet){
                    if(disconnected.get(key)==r.getRoomNumber())
                        disconnected.remove(key);
                }
                r.clearPlayers();
            }
        }
    }

    /**
     * Thus method deregister connection of a client
     * @param client represents the client disconnecting
     */
    synchronized void deregisterConnection(ClientConnection client){
        Logger.logAndPrint("Deregistering "+client.getNickname()+" from server");
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

    /**
     * This method disconnect a client from the server
     * @param client represents the client disconnecting
     */
    void disconnectConnection(ClientConnection client) {
        Logger.logAndPrint("Disconnecting "+client.getNickname()+" from server");
        disconnected.put(client.getNickname(),client.getRoom().getRoomNumber());
        players.remove(client.getNickname());
    }

    /**
     * This method join a client in the room and create e new one in case of need
     * @param client represents the client joining
     */
    synchronized void joinAvailableRoom(ClientConnection client){
        try {
            joinRoom(client);
        } catch (JoinRoomException e) {
            addNewRoom(client);
            Logger.logAndPrint("New room has been created");
        }
    }

    /**
     * This method recover a connection of a client in the room
     * @param client represents the client recovering connection
     */
    void joinRecoveredRoom(ClientConnection client) {
        Room room=rooms.get(disconnected.get(client.getNickname()));
        disconnected.remove(client.getNickname());
        room.recoverClient(client);
    }

    /**
     * This method adds a client to a room
     * @param client represents the client to be add
     * @throws JoinRoomException when the room is full or already exists a client with the same nickname
     */
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

    /**
     * This method create a new room
     * @param client represents the client added in the new room
     */
    private synchronized void addNewRoom(ClientConnection client) {
        Room room = new Room(client);
        rooms.add(room);
        room.setRoomNumber(rooms.size()-1);
        client.setRoom(room);
    }

    /**
     * This method return the connection linked to a certain nickname
     * @param nickname represents the nickname inked to the connection we are looking for
     * @return the connection linked to the nickname given
     */
    ClientConnection getClientConnection(String nickname){
        for (String key:players.keySet()) {
            if(key.equalsIgnoreCase(nickname)){
                return players.get(nickname);
            }
        }
        throw new IllegalArgumentException("No player connected with nickname "+nickname);
    }

    /**
     * This method sets up the parameters useful for the server configuration
     * @throws ParserConfigurationException when an error occurs in parsing file
     * @throws IOException when an error occurs in parsing file
     * @throws SAXException when an error occurs in parsing file
     * @throws URISyntaxException when an error occurs in URI look up
     */
    private static void setUpConfiguration() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        String path;
        String inExecutionFile = new File(Client.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        Logger.logAndPrint(inExecutionFile);
        int last = 0;
        int last2=0;
        for (int i = 0; i < inExecutionFile.length(); i++) {
            if (inExecutionFile.charAt(i) == '/')
                last = i;
        }
        for (int i = 0; i < inExecutionFile.length(); i++) {
            if (inExecutionFile.charAt(i) == '\\')
                last2 = i;
        }
        if(last>last2){
            Logger.logAndPrint(inExecutionFile.substring(0, last + 1));
            path = inExecutionFile.substring(0, last + 1) + "config.xml";
        }else{
            Logger.logAndPrint(inExecutionFile.substring(0, last2 + 1));
            path = inExecutionFile.substring(0, last2 + 1) + "config.xml";
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(path));
        document.normalizeDocument();
        Element root = document.getDocumentElement();
        root.normalize();

        setRMIServerName(root.getElementsByTagName("rmiServerName"));
        setRMIServerPort(root.getElementsByTagName("rmiServerPort"));
        setSocketServerPort(root.getElementsByTagName("socketServerPort"));
        setMinPlayerNumber(root.getElementsByTagName("minPlayerNumber"));
        setMaxPlayerNumber(root.getElementsByTagName("maxPlayerNumber"));
        setSkullNumber(root.getElementsByTagName("skullNumber"));
        setWaitingRoomTimer(root.getElementsByTagName("waitingRoomTimer"));
        setKeepAliveFrequency(root.getElementsByTagName("keepAliveFrequency"));
        setKeepAliveTimer(root.getElementsByTagName("keepAliveTimer"));
        setTurnTimer(root.getElementsByTagName("turnTimer"));
        setQuickMoveTimer(root.getElementsByTagName("quickMoveTimer"));

        Logger.logAndPrint("Configuration completed.");
    }

    /**
     * This method set the number of kills to frenzy mode
     * @param nodeList represents the node of the parsed file tree
     * @throws IOException when an error occurs in parsing file
     */
    private static void setSkullNumber(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        skullNumber =Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    /**
     * This method return next non-empty node
     * @param nodeList represents the node of the parsed file tree
     * @return the next TEXT_NODE
     */
    private static Node getNode(NodeList nodeList){
        int count=0;
        while(count<nodeList.getLength()){
            if(nodeList.item(count).getNodeType()!=Node.TEXT_NODE) return nodeList.item(count);
        }
        return null;
    }

    /**
     * This method sets the timer for the quick action of the game
     * @param nodeList represents the node containing the timer
     * @throws IOException when an error occurs in parsing file
     */
    private static void setQuickMoveTimer(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        quickMoveTimer=Long.parseLong(node.getFirstChild().getNodeValue());
    }

    /**
     * This method sets the timer of the turn
     * @param nodeList represents the node containing the timer
     * @throws IOException when an error occurs in parsing file
     */
    private static void setTurnTimer(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        turnTimer=Long.parseLong(node.getFirstChild().getNodeValue());
    }

    /**
     * This method set the timeout length for the keepAlive method
     * @param nodeList represents the node containing the timer
     * @throws IOException when an error occurs in parsing file
     */
    private static void setKeepAliveTimer(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        keepAliveTimer=Long.parseLong(node.getFirstChild().getNodeValue());
    }

    /**
     * This method set the time for the keepAlive method call frequency
     * @param nodeList represents the node containing the timer
     * @throws IOException when an error occurs in parsing file
     */
    private static void setKeepAliveFrequency(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        keepAliveFrequency=Long.parseLong(node.getFirstChild().getNodeValue());
    }

    /**
     * This method set the time for the waiting room
     * @param nodeList represents the node containing the timer
     * @throws IOException when an error occurs in parsing file
     */
    private static void setWaitingRoomTimer(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        waitingRoomTimer=Long.parseLong(node.getFirstChild().getNodeValue());
    }

    /**
     * This method set the max number of the waiting room capability
     * @param nodeList represents the node containing the number
     * @throws IOException when an error occurs in parsing file
     */
    private static void setMaxPlayerNumber(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        maxPlayerNumber =Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    /**
     * This method set the min number of the waiting room capability
     * @param nodeList represents the node containing the number
     * @throws IOException when an error occurs in parsing file
     */
    private static void setMinPlayerNumber(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        minPlayerNumber =Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    /**
     * This method set the port number for the socket connection
     * @param nodeList represents the node containing the port
     * @throws IOException when an error occurs in parsing file
     */
    private static void setSocketServerPort(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        socketServerPort=Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    /**
     * This method set the port number for the RMI connection
     * @param nodeList represents the node containing the port
     * @throws IOException when an error occurs in parsing file
     */
    private static void setRMIServerPort(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        rmiServerPort=Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    /**
     * This method set the name number for the RMI connection
     * @param nodeList represents the node containing the name
     * @throws IOException when an error occurs in parsing file
     */
    private static void setRMIServerName(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        rmiServerName=node.getFirstChild().getNodeValue();
    }

    static String getRmiServerName() {
        return rmiServerName;
    }

    private static int getRmiServerPort() {
        return rmiServerPort;
    }

    private static int getSocketServerPort() {
        return socketServerPort;
    }

    public static int getMinPlayerNumber() {
        return minPlayerNumber;
    }

    public static int getMaxPlayerNumber() {
        return maxPlayerNumber;
    }

    public static int getSkullNumber(){
        return skullNumber;
    }

    public static long getWaitingRoomTimer() {
        return waitingRoomTimer;
    }

    public static long getKeepAliveFrequency() {
        return keepAliveFrequency;
    }

    public static long getKeepAliveTimer() {
        return keepAliveTimer;
    }

    public static long getTurnTimer() {
        return turnTimer;
    }

    public static long getQuickMoveTimer() {
        return quickMoveTimer;
    }

    public static void main(String[] args) {
        try {
            setUpConfiguration();
            String hostname=Constants.RMI_HOSTNAME;
            Server server = new Server(hostname);
            server.startServer();

            Logger.logAndPrint(Server.getRmiServerName() + " started:");
            Logger.logAndPrint("(RMI: " + Server.getRmiServerPort()+ ", socket: " + Server.getSocketServerPort()+ ")");
        } catch (ServerException e) {
            Logger.logErr(e.getMessage());
        } catch (IOException | URISyntaxException | ParserConfigurationException | SAXException e) {
            Logger.logErr("CONFIG.XML - "+e.getMessage());
        }
    }
}
