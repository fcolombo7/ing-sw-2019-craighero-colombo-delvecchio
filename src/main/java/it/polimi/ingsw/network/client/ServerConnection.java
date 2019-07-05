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
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     *
     * @return the tcp port
     */
    public int getPort() {
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
    public abstract void closeTurn();
    public abstract void selectAction(String action);
    public abstract void movePlayer(String target, int[] newPosition);
    public abstract void discardWeapon(Card weapon);
    public abstract void selectEffect(String effectName);
    public abstract void loadableWeapon(Card weapon);
    public abstract void runAction(int[] newPosition);
    public abstract void selectPlayers(List<List<String>> selected);
    public abstract void selectPowerup(Card powerup);
    public abstract void stopRoutine(boolean stop);
    public abstract void usePowerup(boolean use);
    public abstract void selectWeapon(Card weapon);
    public abstract void counterAttackAnswer(boolean counterAttack);
    public abstract void confirmEndGame();
}
