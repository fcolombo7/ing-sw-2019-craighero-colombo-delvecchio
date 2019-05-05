package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerHandler extends Remote {
    boolean login(String clientName) throws RemoteException;
}
