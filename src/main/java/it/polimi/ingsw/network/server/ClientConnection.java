package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.Room;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.List;

/**
 * This class represents the interface used by the server to communicate with the client
 */
public abstract class ClientConnection{

    /**
     * This attribute represents whether the client is online
     */
    private boolean online;

    /**
     * This attribute represents the nickname linked to the client
     */
    private String nickname;

    /**
     * This attribute represents the motto linked to the client
     */
    private String motto;

    /**
     * This attribute represents the room which the client is part of
     */
    private Room room;

    /**
     * This constructor set false the online attribute
     */
    public ClientConnection(){
        online=false;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    /**
     * This method is called to close the connection with the client
     */
    public abstract void closeConnection();

    /*ROOM METHOD*/

    /**
     * This method is called when a player join the room
     * @param nickname represents the nickname of the player joining the room
     */
    public abstract void joinRoomAdvise(String nickname);

    /**
     * This method is called when a player leave a room
     * @param nickname represents the nickname of the player leaving the room
     */
    public abstract void exitRoomAdvise(String nickname);

    /**
     * This method tell the player that it is the first in the room
     */
    public abstract void firstInRoomAdvise();

    /**
     * This method is called to keep the connection alive verifying if the client still answers
     */
    public abstract void keepAlive();

    /*MATCH METHOD*/

    /**
     * This method is called when a match is created
     * @param players represents the player in the match
     * @param playerTurnNumber represents the number of the turn rotation of the client receiving the message
     */
    public abstract void matchCreation(List<SimplePlayer> players, int playerTurnNumber);

    /**
     * This method is called when an invalid message is received
     * @param msg represents the error message
     */
    public abstract void invalidMessageReceived(String msg);

    /**
     * This method is called when the game board is updated
     * @param gameBoard represents the game board updated
     */
    public abstract void boardUpdate(SimpleBoard gameBoard);

    /**
     * This method is called when the match mode is updated
     * @param players represents the players playing
     * @param gameBoard represents the game board updated
     * @param frenzy represents if the frenzy mode is on
     */
    public abstract void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy);

    /**
     * This method is called when a player is recovered
     * @param players represents the players playing
     * @param gameBoard represents the game board updated
     * @param frenzy represents if the frenzy mode is on
     */
    public abstract void recoveringPlayer(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy);

    /**
     * This method is called when a player is going to respawn
     * @param powerups represents the powerups sent
     * @param colors represents the colors of the powerups sent
     */
    public abstract void respwanRequest(List<Card> powerups,List<Color> colors);

    /**
     * This method is called when a player respawn
     * @param player represents the player respawning
     * @param discardedPowerup represents the powerup chosen to respawn
     * @param color represents the color of the powerup chosen
     */
    public abstract void respwanCompleted(SimplePlayer player, Card discardedPowerup, Color color);

    /**
     * This method is called when an ammo tile is grabbed
     * @param player represents the player grabbing
     * @param grabbedTile represents the tile grabbed
     */
    public abstract void grabbedTile(SimplePlayer player, AmmoTile grabbedTile);

    /**
     * This method is called when a powerup is grabbed
     * @param player represents the player grabbing
     * @param powerup represents the poweruo grabbed
     * @param color represents the color of the powerup grabbed
     */
    public abstract void grabbedPowerup(SimplePlayer player, Card powerup, Color color);

    /**
     * This method is called when a player requests to grab a weapon
     * @param weapons represents the weapon which can be grabbed
     */
    public abstract void grabbableWeapons(List<Card> weapons);

    /**
     * This method is called when a player have to discard a weapon
     * @param weapons represents the the weapons available to discard
     */
    public abstract void discardWeapon(List<Card> weapons);

    /**
     * This method is called when a player grabs a weapon
     * @param player represents the player grabbing
     * @param weapon represents the weapon grabbed
     */
    public abstract void grabbedWeapon(SimplePlayer player, Card weapon);

    /**
     * This method is called when a weapon is reloaded
     * @param player represents the player who has reloaded
     * @param weapon represents the weapon reloaded
     * @param discardedPowerups represents the powerups discarded to reload
     * @param totalCost represents the ammo used to reload
     */
    public abstract void reloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost);

    /**
     * This method is called when a player wants to reload
     * @param weapons represents the weapons reloadable
     */
    public abstract void reloadableWeapons(List<Card> weapons);

    /**
     * This method is called when a player have to choose the action to make
     * @param actions represents the actions available
     */
    public abstract void turnActions(List<String> actions);

    /**
     * This method is called when a turn ends
     */
    public abstract void turnEnd();

    /**
     * This method is called when a player moves
     * @param player represents the player moving
     */
    public abstract void moveAction(SimplePlayer player);

    /**
     * This method is called when a player requests to move
     * @param matrix represents where the player can move
     * @param targetPlayer represents the player to move
     */
    public abstract void moveRequest(MatrixHelper matrix, String targetPlayer);

    /**
     * This method is called when a player receives marks
     * @param player represents the player who has marked
     * @param selected represents the player marked
     * @param value represents the number of marks
     */
    public abstract void markAction(String player, SimplePlayer selected, int value);

    /**
     * This method is called when a player deals damage
     * @param player represents the player dealing damage
     * @param selected represents the player who suffers damage
     * @param damageValue represents the number of damage dealt
     * @param convertedMarks represents the marks converted in damages
     */
    public abstract void damageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks);

    /**
     * This method is called when a player discard a powerup
     * @param player represents the player discarding
     * @param powerup represents the powerup discarded
     */
    public abstract void discardedPowerup(SimplePlayer player, Card powerup);

    /**
     * This method is called when a player has the max number of powerup
     */
    public abstract void fullOfPowerup();

    /**
     * This method is called when a turn starts
     * @param currentPlayer represents the player playing
     */
    public abstract void turnCreation(String currentPlayer);

    /**
     * This method is called when the client has to choose the targets to hit
     * @param selectable represents the set of targets
     * @param target represents the type of target
     */
    public abstract void selectablePlayers(List<List<String>> selectable, SimpleTarget target);

    /**
     * This method is called when a player can use a powerup, asking if he wants to use it
     */
    public abstract void canUsePowerup();

    /**
     * This method is called when a player can use optional effects
     */
    public abstract void canStopRoutine();

    /**
     * This method is called when a player wants to shoot
     * @param usableWeapons represents the weapons usable
     */
    public abstract void usableWeapons(List<Card> usableWeapons);

    /**
     * This method is called to show which effects are available
     * @param effects represents the effects available
     */
    public abstract void availableEffects(List<String> effects);

    /**
     * This method is called when an used effect costs
     * @param player represents the player paying
     * @param discardedPowerups represents the powerup discarded to pay
     * @param usedAmmo represents the ammo used to pay
     */
    public abstract void payEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo);

    /**
     * This method is called when a card is used
     * @param card represents the card used
     */
    public abstract void usedCard(Card card);

    /**
     * This method is called to tell which powerup are available to use
     * @param powerups represents the powerup available
     * @param colors represents the color of the powerup
     */
    public abstract void availablePowerups(List<Card> powerups, List<Color> colors);

    /**
     * This method is called when a run action is completed
     * @param player represents the player running
     * @param newPosition represents the new position of the player
     */
    public abstract void runCompleted(SimplePlayer player, int[] newPosition);

    /**
     * This method is called when a player is running
     * @param matrix represents where the player can run
     */
    public abstract void runRoutine(MatrixHelper matrix);

    /**
     * This method is called when a player recover
     * @param nickname represents the player recovering
     */
    public abstract void recoverAdvise(String nickname);

    /**
     * This method is called when  player can counter attack
     */
    public abstract void canCounterAttack();

    /**
     * This method is called when a player counter attack with a powerup
     * @param currentPlayer represents the player countering
     * @param player represents the player hit
     * @param powerup represents the powerup used
     */
    public abstract void counterAttack(SimplePlayer currentPlayer, SimplePlayer player, Card powerup);

    /**
     * This method is called when the counter attack timeout finishes
     */
    public abstract void counterAttackTimeOut();

    /**
     * This method to notify the disconnection of a client
     */
    public abstract void notifyDisconnetion();

    /**
     * This method is called when the game ends
     * @param players represents the player playing at the end of the game
     */
    public abstract void gameEnd(List<SimplePlayer> players);

    /**
     * This method sends the leader board
     * @param nicknames represents the nicknames of the players
     * @param points represents the points of the players
     */
    public abstract void sendLeaderboard(List<String> nicknames, List<Integer> points);
}