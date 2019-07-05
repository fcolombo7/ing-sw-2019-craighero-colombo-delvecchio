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
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public Server(String hostname) {
        players=new HashMap<>();
        disconnected=new HashMap<>();
        rooms=new ArrayList<>();
        rmiServer=new RMIServer(this,hostname);
        socketServer=new SocketServer(this);
    }

    public void startServer() throws ServerException {
        rmiServer.start(Server.getRmiServerPort());
        socketServer.startServer(Server.getSocketServerPort());
    }

    public synchronized int checkClientLogin(String nickname, ClientConnection client){
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

    public synchronized void deregisterConnection(ClientConnection client){
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

    public void disconnectConnection(ClientConnection client) {
        Logger.logAndPrint("Disconnecting "+client.getNickname()+" from server");
        disconnected.put(client.getNickname(),client.getRoom().getRoomNumber());
        players.remove(client.getNickname());
    }

    public synchronized void joinAvailableRoom(ClientConnection client){
        try {
            joinRoom(client);
        } catch (JoinRoomException e) {
            addNewRoom(client);
            Logger.logAndPrint("New room has been created");
        }
    }

    public void joinRecoveredRoom(ClientConnection client) {
        Room room=rooms.get(disconnected.get(client.getNickname()));
        disconnected.remove(client.getNickname());
        room.recoverClient(client);
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

    private static void setUpConfiguration() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        String path;
        String inExecutionFile = new File(Client.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        //Logger.logAndPrint(inExecutionFile);
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
            path = inExecutionFile.substring(0, last + 1) + "config.xml";
        }else{
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

    private static void setSkullNumber(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        skullNumber =Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    private static Node getNode(NodeList nodeList){
        int count=0;
        while(count<nodeList.getLength()){
            if(nodeList.item(count).getNodeType()!=Node.TEXT_NODE) return nodeList.item(count);
        }
        return null;
    }

    private static void setQuickMoveTimer(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        quickMoveTimer=Long.parseLong(node.getFirstChild().getNodeValue());
    }

    private static void setTurnTimer(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        turnTimer=Long.parseLong(node.getFirstChild().getNodeValue());
    }

    private static void setKeepAliveTimer(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        keepAliveTimer=Long.parseLong(node.getFirstChild().getNodeValue());
    }

    private static void setKeepAliveFrequency(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        keepAliveFrequency=Long.parseLong(node.getFirstChild().getNodeValue());
    }

    private static void setWaitingRoomTimer(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        waitingRoomTimer=Long.parseLong(node.getFirstChild().getNodeValue());
    }

    private static void setMaxPlayerNumber(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        maxPlayerNumber =Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    private static void setMinPlayerNumber(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        minPlayerNumber =Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    private static void setSocketServerPort(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        socketServerPort=Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    private static void setRMIServerPort(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        rmiServerPort=Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    private static void setRMIServerName(NodeList nodeList) throws IOException {
        Node node=getNode(nodeList);
        if(node==null) throw new IOException("INVALID CONFIG.XML");
        rmiServerName=node.getFirstChild().getNodeValue();
    }

    public static String getRmiServerName() {
        return rmiServerName;
    }

    public static int getRmiServerPort() {
        return rmiServerPort;
    }

    public static int getSocketServerPort() {
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

    private static String setHostname() throws SocketException, UnknownHostException {
        String ip="";
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        }
        Logger.logAndPrint("SERVER IP: "+ip);
        return ip;
    }

    public static void main(String[] args) {
        try {
            setUpConfiguration();
            String hostname=setHostname();
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
