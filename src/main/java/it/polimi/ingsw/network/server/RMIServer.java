package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.RMIClientHandler;
import it.polimi.ingsw.network.RMIServerHandler;
import it.polimi.ingsw.network.controller.messages.LoginMessage;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.UUID;

public class RMIServer implements RMIServerHandler, Serializable {

    private static final long serialVersionUID = -6063956040408133376L;
    private transient Server server;
    private transient HashMap<String,String> rmiClients;

    public RMIServer(Server server){
        this.server=server;
        rmiClients=new HashMap<>();
    }
    
    public void start(int portNumber) throws ServerException {
        try{
            Registry registry = LocateRegistry.createRegistry(portNumber);
            RMIServerHandler stub = (RMIServerHandler) UnicastRemoteObject.exportObject(this, portNumber);
            registry.bind(Constants.RMI_SERVER_NAME,stub);
            Logger.log("RMI server started.");
        } catch (AccessException e) {
            throw new ServerException("RMI server not loaded (AccessException):\n"+e.getMessage());
        } catch (RemoteException e) {
            throw new ServerException("RMI server not loaded (RemoteException):\n"+e.getMessage());
        } catch (AlreadyBoundException e) {
            throw new ServerException("RMI server not loaded (AlreadyBoundException):\n"+e.getMessage());
        }
    }

    /*RMI REMOTE METHODS IMPLEMENTATION*/

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

    @Override
    public void boardPreference(String session, int value) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            server.getClientConnection(nick).getRoom().getController().roomPreferenceManager(nick,value);
    }

    /*
    @Override
    public void sendMatchAnswer(String session, MatchAnswer message) throws RemoteException {
        String nick=rmiClients.get(session);
        if(nick!=null)
            ((RMIClientConnection)server.getClientConnection(nick)).getMatchAnswer(message);
    }

     */
}
