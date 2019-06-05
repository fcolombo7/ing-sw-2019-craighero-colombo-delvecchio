package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.ui.AdrenalineUI;

import java.util.List;

public abstract class ServerConnection {
    private static final long serialVersionUID = 8681502936497815461L;
    private AdrenalineUI ui;
    private String nickname;
    private String hostname;
    private int port;

    ServerConnection(String hostname, int port,AdrenalineUI ui){
        this.ui=ui;
        this.hostname=hostname;
        this.port=port;
    }

    public AdrenalineUI getUi() {
        return ui;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    /*------ CLIENT --> SERVER ------*/
    public abstract String login(String nickname, String motto);
    public abstract void logout();
    public abstract void boardPreference(int value);
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
}
