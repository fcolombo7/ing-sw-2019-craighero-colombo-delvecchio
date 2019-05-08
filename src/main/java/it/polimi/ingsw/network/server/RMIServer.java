package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.ClientConnection;
import it.polimi.ingsw.network.RMIClientConnection;
import it.polimi.ingsw.utils.Costants;

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
            registry.bind(Costants.RMI_SERVER_NAME,stub);
            System.out.println("RMI server started.");
        } catch (AccessException e) {
            throw new ServerException("RMI server not loaded (AccessException):\n"+e.getMessage());
        } catch (RemoteException e) {
            throw new ServerException("RMI server not loaded (RemoteException):\n"+e.getMessage());
        } catch (AlreadyBoundException e) {
            throw new ServerException("RMI server not loaded (AlreadyBoundException):\n"+e.getMessage());
        }
    }

    @Override
    public synchronized String login(String clientName, String clientMotto, RMIClientHandler client) {
        System.out.println("[Received a login request form RMI]");
        RMIClientConnection clientConnection = new RMIClientConnection(server,client);
        String msg;
        if(server.checkClientLogin(clientName,clientConnection)){
            clientConnection.setNickname(clientName);
            clientConnection.setMotto(clientMotto);
            server.joinAvailableRoom(clientConnection);
            String session= UUID.randomUUID().toString();
            rmiClients.put(session,clientName);
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
}
