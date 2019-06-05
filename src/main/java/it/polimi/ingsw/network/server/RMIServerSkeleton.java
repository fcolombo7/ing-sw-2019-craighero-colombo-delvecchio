package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.RMIClientHandler;
import it.polimi.ingsw.network.RMIServerHandler;
import it.polimi.ingsw.utils.Logger;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RMIServerSkeleton extends UnicastRemoteObject implements RMIServerHandler {
    private static final long serialVersionUID = 7000106595683551762L;
    private transient  Server server;
    private transient HashMap<String,String> rmiClients;

    protected RMIServerSkeleton(Server server) throws RemoteException {
        super();
        this.server=server;
        rmiClients=new HashMap<>();
    }


    @Override
    public synchronized String login(String nickname, String motto, RMIClientHandler client) {
        Logger.log("[Received a login request form RMI]");
        RMIClientConnection clientConnection = new RMIClientConnection(client,this);
        if(server.checkClientLogin(nickname,clientConnection)){
            clientConnection.setNickname(nickname);
            clientConnection.setMotto(motto);
            server.joinAvailableRoom(clientConnection);
            String session= UUID.randomUUID().toString();
            rmiClients.put(session,nickname);
            clientConnection.setSession(session);
            return session;
        }
        return null;
    }

    @Override
    public boolean deregister(String session) {
        String nick=rmiClients.get(session);
        if(nick==null) return false;
        server.deregisterConnection(server.getClientConnection(nick));
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
            server.getClientConnection(nick).getRoom().getController().respawnPlayer(nick,powerup);
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

    @Override
    public void selectPlayers(String session, List<List<String>> selected) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().selectPlayers(selected);
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
}
