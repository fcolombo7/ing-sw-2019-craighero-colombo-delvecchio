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

public abstract class ClientConnection{
    private boolean online;
    private String nickname;
    private String motto;
    private Room room;

    ClientConnection(){
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

    public abstract void closeConnection();

    /*ROOM METHOD*/
    public abstract void joinRoomAdvise(String nickname);

    public abstract void exitRoomAdvise(String nickname);

    public abstract void firstInRoomAdvise();

    public abstract void keepAlive();

    /*MATCH METHOD*/
    public abstract void matchCreation(List<SimplePlayer> players, int playerTurnNumber);

    public abstract void invalidMessageReceived(String msg);

    public abstract void boardUpdate(SimpleBoard gameBoard);

    public abstract void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy);

    public abstract void respwanRequest(List<Card> powerups);

    public abstract void respwanCompleted(SimplePlayer player, Card discardedPowerup);

    public abstract void grabbedTile(SimplePlayer player, AmmoTile grabbedTile);

    public abstract void grabbedPowerup(SimplePlayer player, Card powerup);

    public abstract void grabbableWeapons(List<Card> weapons);

    public abstract void discardWeapon(List<Card> weapons);

    public abstract void grabbedWeapon(SimplePlayer player, Card weapon);

    public abstract void reloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost);

    public abstract void reloadableWeapons(List<Card> weapons);

    public abstract void turnActions(List<String> actions);

    public abstract void turnEnd();

    public abstract void moveAction(SimplePlayer player);

    public abstract void moveRequest(MatrixHelper matrix, String targetPlayer);

    public abstract void markAction(String player, SimplePlayer selected, int value);

    public abstract void damageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks);

    public abstract void discardedPowerup(SimplePlayer player, Card powerup);

    public abstract void turnCreation(String currentPlayer);

    public abstract void selectablePlayers(List<List<String>> selectable, SimpleTarget target);

    public abstract void canUsePowerup();

    public abstract void canStopRoutine();

    public abstract void usableWeapons(List<Card> usableWeapons);

    public abstract void availableEffects(List<String> effects);

    public abstract void payEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo);

    public abstract void usedCard(Card card);

    public abstract void availablePowerups(List<Card> powerups);

    public abstract void runCompleted(SimplePlayer player, int[] newPosition);

    public abstract void runRoutine(MatrixHelper matrix);
}