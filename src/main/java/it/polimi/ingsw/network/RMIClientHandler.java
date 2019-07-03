package it.polimi.ingsw.network;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.utils.MatrixHelper;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIClientHandler extends Remote {

    /*ROOM REMOTE METHOD*/
    void joinPlayer(String nickname) throws RemoteException;
    void exitPlayer(String nickname) throws RemoteException;
    void firstInRoom() throws RemoteException;
    //TODO: ELIMINARE
    void recoverPlayer(String nickname) throws RemoteException;
    void keepAlive() throws RemoteException;

    /*MATCH REMOTE METHOD*/
    void matchCreation(List<SimplePlayer> players, int playerTurnNumber) throws RemoteException;
    void invalidMessageReceived(String msg) throws RemoteException;
    void boardUpdate(SimpleBoard gameBoard) throws RemoteException;
    void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard,boolean frenzy) throws RemoteException;
    void respwanRequest(List<Card> powerups,List<Color> colors) throws RemoteException;
    void respwanCompleted(SimplePlayer player, Card discardedPowerup, Color color) throws RemoteException;
    void grabbedTile(SimplePlayer player, AmmoTile grabbedTile) throws RemoteException;
    void grabbedPowerup(SimplePlayer player, Card powerup, Color color)throws RemoteException;
    void grabbableWeapons(List<Card> weapons)throws RemoteException;
    void discardWeapon(List<Card> weapons)throws RemoteException;
    void grabbedWeapon(SimplePlayer player, Card weapon)throws RemoteException;
    void reloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) throws RemoteException;
    void reloadableWeapons(List<Card> weapons) throws RemoteException;
    void turnActions(List<String> actions) throws RemoteException;
    void turnEnd() throws RemoteException;
    void moveAction(SimplePlayer player) throws RemoteException;
    void moveRequest(MatrixHelper matrix, String targetPlayer) throws RemoteException;
    void markAction(String player, SimplePlayer target, int value)  throws RemoteException;
    void damageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks)  throws RemoteException;
    void discardedPowerup(SimplePlayer player, Card powerup) throws RemoteException;
    void turnCreation(String currentPlayer)  throws RemoteException;
    void selectablePlayers(List<List<String>> selectable, SimpleTarget target) throws RemoteException;
    void canUsePowerup() throws RemoteException;
    void canStopRoutine() throws RemoteException;
    void usableWeapons(List<Card> usableWeapons) throws RemoteException;
    void availableEffects(List<String> effects) throws RemoteException;
    void payEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) throws RemoteException;
    void usedCard(Card card) throws RemoteException;
    void availablePowerups(List<Card> powerups, List<Color> colors) throws RemoteException;
    void runCompleted(SimplePlayer player, int[] newPosition) throws RemoteException;
    void runRoutine(MatrixHelper matrix) throws RemoteException;
    void wakeUpPlayer(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) throws RemoteException;
    void fullOfPowerup() throws RemoteException;
    void canCounterAttack() throws RemoteException;
    void counterAttack(SimplePlayer currentPlayer, SimplePlayer player, Card powerup) throws RemoteException;
    void counterAttackTimeOut() throws RemoteException;
    void notifyDisconnection() throws RemoteException;
    void gameEnd(List<SimplePlayer> players) throws RemoteException;
    void sendLeaderboard(List<String> nicknames, List<Integer> points) throws RemoteException;
}
