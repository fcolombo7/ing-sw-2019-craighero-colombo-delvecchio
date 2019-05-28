package it.polimi.ingsw.network;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.LoginMessage;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.network.controller.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.network.controller.messages.room.ExitMessage;
import it.polimi.ingsw.network.controller.messages.room.JoinMessage;
import it.polimi.ingsw.network.controller.messages.room.RoomMessage;
import it.polimi.ingsw.network.server.RMIClientHandler;
import it.polimi.ingsw.network.server.RMIServerHandler;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.MatrixHelper;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class TestRMIClient implements RMIClientHandler {

    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {
        Scanner stdin = new Scanner(System.in);
        Registry registry = LocateRegistry.getRegistry(Constants.RMI_PORT);

        System.out.print("RMI registry bindings: ");
        String[] e = registry.list();
        for (int i = 0; i < e.length; i++)
            System.out.println(e[i]);

        System.out.print("nickname: ");
        String nickname=stdin.nextLine();
        System.out.print("motto: ");
        String motto=stdin.nextLine();

        TestRMIClient rmiClient=new TestRMIClient();
        RMIClientHandler stub= (RMIClientHandler) UnicastRemoteObject.exportObject(rmiClient, Constants.RMI_PORT+1);
        registry.bind(nickname,stub);


        String remoteObjectName = Constants.RMI_SERVER_NAME;
        RMIServerHandler serverRMI = (RMIServerHandler) registry.lookup(remoteObjectName);

        String session = serverRMI.login(new LoginMessage(nickname,motto),stub);
        System.out.println(session);
    }

    @Override
    public void onJoin(String nickname) throws RemoteException {
        System.out.println("User "+ nickname+" has join the room...");
    }

    @Override
    public void onExit(String nickname) throws RemoteException {
        System.out.println("User "+ nickname+" has left the room...");
    }

    @Override
    public void firstInRoom() throws RemoteException {
        System.out.println("You are the first player in the room!");
    }

    @Override
    public void matchCreation(List<SimplePlayer> players, int playerTurnNumber) throws RemoteException {
        System.out.println("MATCH CREATED: ("+playerTurnNumber+")");
    }

    @Override
    public void invalidMessageReceived(String msg) throws RemoteException {
        System.out.println("INVALID MESSAGE RECEIVED: ("+msg+")");
    }

    @Override
    public void boardUpdate(SimpleBoard gameBoard) throws RemoteException {
        System.out.println("BOARD UPDATE: ("+gameBoard.toString()+")");
    }

    @Override
    public void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) throws RemoteException {
        System.out.println("MATCH UPDATE: ("+players.size()+", "+gameBoard.toString()+")");
    }

    @Override
    public void respwanRequest(List<Card> powerups) throws RemoteException {
        System.out.println("RESPAWN REQUEST: ("+powerups.toString()+")");
    }

    @Override
    public void respwanCompleted(SimplePlayer player, Card discardedPowerup) throws RemoteException {
        System.out.println("RESPAWN COMPLETED: ("+player.getNickname()+")");
    }

    @Override
    public void grabbedTile(SimplePlayer player, AmmoTile grabbedTile) throws RemoteException {

    }

    @Override
    public void grabbedPowerup(SimplePlayer player, Card powerup) throws RemoteException {

    }

    @Override
    public void grabbableWeapons(List<Card> weapons) throws RemoteException {

    }

    @Override
    public void discardWeapon(List<Card> weapons) throws RemoteException {

    }

    @Override
    public void grabbedWeapon(SimplePlayer player, Card weapon) throws RemoteException {

    }

    @Override
    public void reloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) throws RemoteException {

    }

    @Override
    public void reloadableWeapons(List<Card> weapons) throws RemoteException {

    }

    @Override
    public void turnActions(List<String> actions) throws RemoteException {

    }

    @Override
    public void turnEnd() throws RemoteException {

    }

    @Override
    public void moveAction(SimplePlayer player) throws RemoteException {

    }

    @Override
    public void moveRequest(MatrixHelper matrix, String targetPlayer) throws RemoteException {

    }

    @Override
    public void markAction(String player, SimplePlayer player1, int value) throws RemoteException {

    }

    @Override
    public void damageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) throws RemoteException {

    }

    @Override
    public void discardedPowerup(SimplePlayer player, Card powerup) throws RemoteException {

    }

    @Override
    public void turnCreation(String currentPlayer) throws RemoteException {

    }

    @Override
    public void selectablePlayers(List<List<String>> selectable, SimpleTarget target) throws RemoteException {

    }

    @Override
    public void canUsePowerup() throws RemoteException {

    }

    @Override
    public void canStopRoutine() throws RemoteException {

    }

    @Override
    public void usableWeapons(List<Card> usableWeapons) throws RemoteException {

    }

    @Override
    public void availableEffects(List<String> effects) throws RemoteException {

    }

    @Override
    public void payEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) throws RemoteException {

    }

    @Override
    public void usedCard(Card card) throws RemoteException {

    }

    @Override
    public void availablePowerups(List<Card> powerups) throws RemoteException {

    }

    @Override
    public void runCompleted(SimplePlayer player, int[] newPosition) throws RemoteException {

    }

    @Override
    public void runRoutine(MatrixHelper matrix) throws RemoteException {

    }

}
