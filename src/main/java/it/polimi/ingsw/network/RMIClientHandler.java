package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

//String will be changed with MessageClass
public interface RMIClientHandler extends Remote {

    void actionRequest(String msg) throws RemoteException;

    boolean hasAnswered() throws RemoteException;

    String actionAnswer() throws RemoteException;

}
