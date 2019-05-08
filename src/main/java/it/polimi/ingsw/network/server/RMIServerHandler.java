package it.polimi.ingsw.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerHandler extends Remote {
    String login(String clientName, String motto, RMIClientHandler client) throws RemoteException;
}
