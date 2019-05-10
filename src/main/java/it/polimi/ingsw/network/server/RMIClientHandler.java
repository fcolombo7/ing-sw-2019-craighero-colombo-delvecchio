package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.messages.MatchMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientHandler extends Remote {

    void sendMatchMessage(MatchMessage message) throws RemoteException;

}
