package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class RMIServer {

    private static final long serialVersionUID = -6063956040408133376L;
    private Server server;
    private String hostname;

    public RMIServer(Server server, String hostname){
        this.server=server;
        this.hostname=hostname;
    }

    public void start(int portNumber) throws ServerException {
        try{
            //System.setProperty("java.rmi.server.hostname",hostname);
            //RMIServerHandler stub = (RMIServerHandler) UnicastRemoteObject.exportObject(this, portNumber);
            //registry.rebind(Constants.RMI_SERVER_NAME,stub);

            System.setProperty("java.rmi.server.codebase","file://");
            startRegistry(portNumber);
            String name="//"+this.hostname+":"+portNumber+"/"+Constants.RMI_SERVER_NAME;
            Naming.rebind("//"+this.hostname+":"+portNumber+"/"+Constants.RMI_SERVER_NAME,new RMIServerSkeleton(server));
            Logger.log("RMI server started. ["+name+"]");
        } catch (AccessException e) {
            throw new ServerException("RMI server not loaded (AccessException):\n"+e.getMessage());
        } catch (RemoteException e) {
            throw new ServerException("RMI server not loaded (RemoteException):\n"+e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void startRegistry(int portNumber) throws RemoteException {
        try{
            LocateRegistry.getRegistry(portNumber);
            LocateRegistry.getRegistry(portNumber).list();
        } catch (RemoteException e) {
            LocateRegistry.createRegistry(portNumber);
        }
    }

    /*RMI REMOTE METHODS IMPLEMENTATION*/

}