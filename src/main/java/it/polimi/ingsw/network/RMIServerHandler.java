package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Card;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIServerHandler extends Remote {

    String login(String nickname, String motto, RMIClientHandler client) throws RemoteException;

    boolean deregister(String session) throws RemoteException;

    void boardPreference(String session, int value) throws RemoteException;
    void respawnPlayer(String session, String sender, Card powerup) throws RemoteException;
    void closeTurn(String session, String sender) throws RemoteException;
    void selectAction(String session, String action) throws RemoteException;
    void movePlayer(String session, String target,int[] newPosition) throws RemoteException;
    void discardWeapon(String session, Card weapon) throws RemoteException;
    void selectEffect(String session, String effectName) throws RemoteException;
    void loadableWeapon(String session, Card weapon) throws RemoteException;
    void runAction(String session, int[] newPosition) throws RemoteException;
    void selectPlayers(String session, List<List<String>> selected) throws RemoteException;
    void selectPowerup(String session, Card powerup) throws RemoteException;
    void stopRoutine(String session, boolean stop) throws RemoteException;
    void usePowerup(String session, boolean use) throws RemoteException;
    void selectWeapon(String session, Card weapon) throws RemoteException;
}
