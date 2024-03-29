package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.RMIClientHandler;
import it.polimi.ingsw.network.RMIServerHandler;
import it.polimi.ingsw.ui.gui.Client;
import it.polimi.ingsw.utils.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This class represents the rmi server
 */
public class RMIServer  implements RMIServerHandler{

    /**
     * This attribute represents the server
     */
    private Server server;
    /**
     * This attribute represents the hostname
     */
    private String hostname;
    /**
     * This attribute represents an hashmap that associates the session to the client nickname
     */
    private HashMap<String,String> rmiClients;

    /**
     * this constructor initializes the rmi server
     * @param server represents the server
     * @param hostname represents the hostname
     */
    public RMIServer(Server server, String hostname){
        this.server=server;
        this.hostname=hostname;
        rmiClients=new HashMap<>();
    }


    public void start(int portNumber) throws ServerException {
        try{
            Logger.logAndPrint("Present Project Directory : "+ System.getProperty("user.dir"));
            Logger.logAndPrint("Composing codebase path: "+ "file://"+new File(Client.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
            System.setProperty("java.rmi.server.hostname",hostname);
            System.setProperty("java.rmi.server.codebase","file://"+System.getProperty("user.dir")+"/target/classes");
            startRegistry(portNumber);
            RMIServerHandler stub = (RMIServerHandler) UnicastRemoteObject.exportObject(this, portNumber);
            String name="rmi//"+this.hostname+":"+portNumber+"/"+Server.getRmiServerName();
            Naming.rebind("//"+this.hostname+":"+portNumber+"/"+Server.getRmiServerName(),stub);
            Logger.logAndPrint("RMI server started. ["+name+"]");
        } catch (AccessException e) {
            throw new ServerException("RMI server not loaded (AccessException):\n"+e.getMessage());
        } catch (RemoteException e) {
            throw new ServerException("RMI server not loaded (RemoteException):\n"+e.getMessage());
        } catch (MalformedURLException e) {
            throw new ServerException("RMI server not loaded (MalformedURLException):\n"+e.getMessage());
        } catch (URISyntaxException e) {
            throw new ServerException("RMI server not loaded (URISyntaxException):\n"+e.getMessage());
        }
    }

    /**
     * This method starts the registry for rmi connection
     * @param portNumber represents the rmi port number
     * @throws RemoteException represents the exception thrown when the registry were not created
     */
    private void startRegistry(int portNumber) throws RemoteException {
        try{
            LocateRegistry.getRegistry(portNumber);
            LocateRegistry.getRegistry(portNumber).list();
        } catch (RemoteException e) {
            LocateRegistry.createRegistry(portNumber);
        }
    }

    /*RMI REMOTE METHODS IMPLEMENTATION*/

    @Override
    public synchronized String login(String nickname, String motto, RMIClientHandler client) {
        Logger.logAndPrint("[Received a login request form RMI]");
        RMIClientConnection clientConnection = new RMIClientConnection(client,this);
        int response=server.checkClientLogin(nickname,clientConnection);
        if(response==1||response==2){
            clientConnection.setNickname(nickname);
            clientConnection.setMotto(motto);
            String session= UUID.randomUUID().toString();
            rmiClients.put(session,nickname);
            clientConnection.setSession(session);
            if(response==1)
                server.joinAvailableRoom(clientConnection);
            else
                server.joinRecoveredRoom(clientConnection);
            return session;
        }
        return null;
    }

    @Override
    public boolean deregister(String session) {
        String nick=rmiClients.get(session);
        if(nick==null) return false;
        ClientConnection connection= server.getClientConnection(nick);
        if(connection.getRoom().isPlaying())
            server.disconnectConnection(connection);
        else
            server.deregisterConnection(connection);
        return true;
    }

    /*-------- MATCH ANSWER METHODS CALLED ON CLIENT --------*/
    @Override
    public void boardPreference(String session, int value) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().roomPreferenceManager(nick,value);
    }

    @Override
    public void respawnPlayer(String session, Card powerup) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().respawnPlayer(nick,powerup,false);
    }

    @Override
    public void closeTurn(String session) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().closeTurn(nick);
    }

    @Override
    public void selectAction(String session, String action) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().selectAction(action);
    }

    @Override
    public void movePlayer(String session, String target, int[] newPosition) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().movePlayer(target,newPosition);
    }

    @Override
    public void discardWeapon(String session, Card weapon) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().discardWeapon(weapon);
    }

    @Override
    public void selectEffect(String session, String effectName) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().selectEffect(effectName);
    }

    @Override
    public void loadableWeapon(String session, Card weapon) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().loadableWeapon(weapon);
    }

    @Override
    public void runAction(String session, int[] newPosition) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().runAction(newPosition);
    }

    //TODO
    @Override
    public void selectPlayers(String session, String selected) throws RemoteException {
        Gson gson= new Gson();
        Logger.logAndPrint(selected);
        List<List<String>> sList=gson.fromJson(selected, ArrayList.class);
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().selectPlayers(sList);
    }

    @Override
    public void selectPowerup(String session, Card powerup) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().selectPowerup(powerup);
    }

    @Override
    public void stopRoutine(String session, boolean stop) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().stopRoutine(stop);
    }

    @Override
    public void usePowerup(String session, boolean use) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().usePowerup(use);
    }

    @Override
    public void selectWeapon(String session, Card weapon) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().selectWeapon(weapon);
    }

    @Override
    public void counterAttackAnswer(String session, boolean counterAttack) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().counterAttackAnswer(nick,counterAttack);
    }

    @Override
    public void confirmEndGame(String session) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().gameEndAck(nick);
    }

}
