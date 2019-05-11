package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.messages.matchanswer.MatchAnswer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerHandler extends Remote {

    String login(String clientName, String motto, RMIClientHandler client) throws RemoteException;

    boolean deregister(String session) throws RemoteException;

    void sendMatchAnswer(String session, MatchAnswer message) throws RemoteException;

}
