package it.polimi.ingsw.ui;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.List;

public interface AdrenalineUI {

    /*------- METHODS TO MANAGE MESSAGES FROM SERVER -------*/

    /*ROOM METHOD*/
    void onJoinRoomAdvise(String nickname);

    void onExitRoomAdvise(String nickname);

    void onFirstInRoomAdvise();

    void onPingAdvise();

    /*MATCH METHOD*/
    void onMatchCreation(List<SimplePlayer> players, int playerTurnNumber);

    void onInvalidMessageReceived(String msg);

    void onBoardUpdate(SimpleBoard gameBoard);

    void onMatchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy);

    void onRespwanRequest(List<Card> powerups);

    void onRespwanCompleted(SimplePlayer player, Card discardedPowerup);

    void onGrabbedTile(SimplePlayer player, AmmoTile grabbedTile);

    void onGrabbedPowerup(SimplePlayer player, Card powerup);

    void onGrabbableWeapons(List<Card> weapons);

    void onDiscardWeapon(List<Card> weapons);

    void onGrabbedWeapon(SimplePlayer player, Card weapon);

    void onReloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost);

    void onReloadableWeapons(List<Card> weapons);

    void onTurnActions(List<String> actions);

    void onTurnEnd();

    void onMoveAction(SimplePlayer player);

    void onMoveRequest(MatrixHelper matrix, String targetPlayer);

    void onMarkAction(String player, SimplePlayer selected, int value);

    void onDamageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks);

    void onDiscardedPowerup(SimplePlayer player, Card powerup);

    void onTurnCreation(String currentPlayer);

    void onSelectablePlayers(List<List<String>> selectable, SimpleTarget target);

    void onCanUsePowerup();

    void onCanStopRoutine();

    void onUsableWeapons(List<Card> usableWeapons);

    void onAvailableEffects(List<String> effects);

    void onPayEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo);

    void onUsedCard(Card card);

    void onAvailablePowerups(List<Card> powerups);

    void onRunCompleted(SimplePlayer player, int[] newPosition);

    void onRunRoutine(MatrixHelper matrix);

}
