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

    /**
     * this method is used when a player join the room
     * @param nickname the player who join
     * @throws RemoteException
     */
    void joinPlayer(String nickname) throws RemoteException;

    /**
     * This method is used to handle the exit of a player
     * @param nickname the player who exit
     * @throws RemoteException
     */
    void exitPlayer(String nickname) throws RemoteException;

    /**
     * This method is used to handle the first player in a room
     * @throws RemoteException
     */
    void firstInRoom() throws RemoteException;

    /**
     * This method is used to handle the recover of a player
     * @param nickname the player who is recovered
     * @throws RemoteException
     */
    void recoverPlayer(String nickname) throws RemoteException;

    /**
     * This method is used to handle the keep alive
     * @throws RemoteException
     */
    void keepAlive() throws RemoteException;

    /*MATCH REMOTE METHOD*/

    /**
     * This method is used to send the match creation
     * @param players all the players
     * @param playerTurnNumber the turn of the player who receive the message
     * @throws RemoteException
     */
    void matchCreation(List<SimplePlayer> players, int playerTurnNumber) throws RemoteException;

    /**
     * This method is used to send the invalid message
     * @param msg the invalid message
     * @throws RemoteException
     */
    void invalidMessageReceived(String msg) throws RemoteException;

    /**
     * This method is used to send the board update
     * @param gameBoard the board of the match
     * @throws RemoteException
     */
    void boardUpdate(SimpleBoard gameBoard) throws RemoteException;

    /**
     * This method is used to send the match update
     * @param players all the players of the match
     * @param gameBoard the board of the match
     * @param frenzy if the game is in frezy mode
     * @throws RemoteException
     */
    void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard,boolean frenzy) throws RemoteException;

    /**
     * This method is used to send the respawn request
     * @param powerups the available powerups
     * @param colors the powerups color
     * @throws RemoteException
     */
    void respwanRequest(List<Card> powerups,List<Color> colors) throws RemoteException;

    /**
     * This method is used to send the respawn message
     * @param player the player respawned
     * @param discardedPowerup the discarded powerup
     * @param color representing the color
     * @throws RemoteException
     */
    void respwanCompleted(SimplePlayer player, Card discardedPowerup, Color color) throws RemoteException;

    /**
     * This method is used to send the grabbed tile
     * @param player the current player
     * @param grabbedTile the grabbed tile
     * @throws RemoteException
     */
    void grabbedTile(SimplePlayer player, AmmoTile grabbedTile) throws RemoteException;

    /**
     * This method is used to send the grabbed powerups
     * @param player the current player
     * @param powerup the powerup grabbed
     * @param color the powerup color
     * @throws RemoteException
     */
    void grabbedPowerup(SimplePlayer player, Card powerup, Color color)throws RemoteException;

    /**
     * This method is used to send the grabbable weapons
     * @param weapons the grabbable weapons
     * @throws RemoteException
     */
    void grabbableWeapons(List<Card> weapons)throws RemoteException;

    /**
     * This method is used to send the weapon to discard
     * @param weapons the weaopon to discard
     * @throws RemoteException
     */
    void discardWeapon(List<Card> weapons)throws RemoteException;

    /**
     * This method is used to send the grabbed weapon
     * @param player the current player
     * @param weapon the grabbed weapon
     * @throws RemoteException
     */
    void grabbedWeapon(SimplePlayer player, Card weapon)throws RemoteException;

    /**
     * This method is used to send the reloaded weapon
     * @param player the current player
     * @param weapon the selected weapon
     * @param discardedPowerups the discarded powerups
     * @param totalCost the total cost spent
     * @throws RemoteException
     */
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
