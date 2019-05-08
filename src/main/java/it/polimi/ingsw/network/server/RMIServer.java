package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utils.Costants;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RMIServer implements RMIServerHandler, Serializable {

    private transient Server server;
    private transient HashMap<String,String> rmiClients;

    public RMIServer(Server server){
        this.server=server;
    }
    
    public void start(int portNumber) throws ServerException {
        try{
            Registry registry = LocateRegistry.createRegistry(portNumber);
            RMIServerHandler stub = (RMIServerHandler) UnicastRemoteObject.exportObject(this, portNumber);
            registry.bind(Costants.RMI_SERVER_NAME,stub);
            //UnicastRemoteObject.exportObject(this, portNumber);
            System.out.println("RMI server started.");
        } catch (AccessException e) {
            throw new ServerException("RMI server not loaded (AccessException):\n"+e.getMessage());
        } catch (RemoteException e) {
            throw new ServerException("RMI server not loaded (RemoteException):\n"+e.getMessage());
        } catch (AlreadyBoundException e) {
            throw new ServerException("RMI server not loaded (AlreadyBoundException):\n"+e.getMessage());
        }
    }

    private Registry setUpRegistry(int portNumber) throws ServerException {

        try {
            return LocateRegistry.getRegistry();
        } catch (RemoteException e) {
            System.err.println("Registry not found at port "+portNumber);
        }
        try {
            return LocateRegistry.createRegistry(portNumber);
        } catch (RemoteException e) {
            System.err.println("Registry already exists at port "+portNumber);
        }


        throw new ServerException("Cannot get RMI registry at port " +portNumber);
    }

    @Override
    public synchronized String login(String clientName, String motto, RMIClientHandler client) throws RemoteException {
        return null;
    }
}
