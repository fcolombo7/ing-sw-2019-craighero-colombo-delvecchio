package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.ui.AdrenalineUI;

import java.util.List;

/**
 * This abstract class represent the connection client --> server
 */
public abstract class ServerConnection {
    /**
     * This atributes contains the UI (CLI or gui)
     */
    private AdrenalineUI ui;

    /**
     * This attributes contains the nickname which is the id of the connection
     */
    private String nickname;

    /**
     * This attribute contains the IP of the server
     */
    private String hostname;

    /**
     * This attribute contains the port of the connection
     */
    private int port;

    /**
     * This constructor instantiates a ServerConnection
     * @param hostname server ip
     * @param port port using during the connection
     * @param ui user interface associated
     */
    ServerConnection(String hostname, int port,AdrenalineUI ui){
        this.ui=ui;
        this.hostname=hostname;
        this.port=port;
    }

    /**
     * This method returns the UI
     */
    public AdrenalineUI getUi() {
        return ui;
    }

    /**
     * This method sets the nickname
     * @param nickname the nickname of the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     *  This method returns the nickname
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *  This method returns the server IP
     * @return the hostname
     */
    String getHostname() {
        return hostname;
    }

    /**
     * This method return the TCP port used during the connection
     * @return the tcp port
     */
    int getPort() {
        return port;
    }

    /*------ CLIENT --> SERVER ------*/

    /**
     * This method is used to send a login request to the server
     * @param nickname represents the nicknames of the player
     * @param motto represents the motto of the player
     * @return a String which contains the answer
     */
    public abstract String login(String nickname, String motto);

    /**
     * This method is used to send a logout request
     */
    public abstract void logout();

    /**
     * This method is used toi send the game board preference to the server
     * @param value represents the map number
     */
    public abstract void boardPreference(int value);

    /**
     * This method is used to send the discarded card used to respawn the player
     * @param powerup the discarded card used to respawn the player
     */
    public abstract void respawnPlayer(Card powerup);

    /**
     * This method send the close turn request to the server
     */
    public abstract void closeTurn();

    /**
     * This method is used to send the selected action you want to perform in the turn
     * @param action the selected action you want to perform in the turn
     */
    public abstract void selectAction(String action);

    /**
     * This method is used to send a move response
     * @param target the target to move
     * @param newPosition the target position
     */
    public abstract void movePlayer(String target, int[] newPosition);

    /**
     * This method is used to send a discard weapon message
     * @param weapon the weapon discarded
     */
    public abstract void discardWeapon(Card weapon);

    /**
     * This method is used to send the selected effect
     * @param effectName representing the name of the effect selected
     */
    public abstract void selectEffect(String effectName);

    /**
     * This method is used to send to the server the weapon to load
     * @param weapon the weapon to load
     */
    public abstract void loadableWeapon(Card weapon);

    /**
     * This method is used to send to the server the new position due to the execution of a run routine
     * @param newPosition representing the new poition of the player
     */
    public abstract void runAction(int[] newPosition);

    /**
     * This method is used to send the players selected representing who will be the target of the effect selected
     * @param selected players selected, target of the effect
     */
    public abstract void selectPlayers(List<List<String>> selected);

    /**
     * This method is used to send to the server the selected powerup
     * @param powerup the powerup selected, which will be used on the server
     */
    public abstract void selectPowerup(Card powerup);

    /**
     * this method is used to send to the server the answer of the user to continue or not during the shooting routine
     * @param stop a boolean representing the answer sended to the server
     */
    public abstract void stopRoutine(boolean stop);

    /**
     * This method send to the server the answer of the user to use or not a powerup during the shooting routine
     * @param use a boolean representing the answer sended to the server
     */
    public abstract void usePowerup(boolean use);

    /**
     * This method is used to send to the server the weapon the user want to use
     * @param weapon the weapon the user want to use
     */
    public abstract void selectWeapon(Card weapon);

    /**
     * This method is used to send to the server if you want counter attack or not
     * @param counterAttack the answer sended to the server
     */
    public abstract void counterAttackAnswer(boolean counterAttack);

    /**
     * This method is sent to the server as answer to the game end.
     * If sended the server will send to the user the leaderboard.
     */
    public abstract void confirmEndGame();
}
