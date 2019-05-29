package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerHandler extends Remote {

    String login(String nickname, String motto, RMIClientHandler client) throws RemoteException;

    boolean deregister(String session) throws RemoteException;

    void boardPreference(String session, int value) throws RemoteException;

}
