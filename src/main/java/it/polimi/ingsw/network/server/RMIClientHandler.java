package it.polimi.ingsw.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientHandler extends Remote {

    void actionRequest(String msg) throws RemoteException;

    boolean hasAnswered() throws RemoteException;

    String actionAnswer() throws RemoteException;

}
