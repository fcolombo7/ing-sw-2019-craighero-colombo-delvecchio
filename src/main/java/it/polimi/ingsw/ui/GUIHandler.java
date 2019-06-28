package it.polimi.ingsw.ui;

import it.polimi.ingsw.GUI.MainWindow;
import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.List;

public class GUIHandler implements AdrenalineUI {

    @Override
    public void onJoinRoomAdvise(String nickname) {
        MainWindow.onJoinRoomAdvise(nickname);
    }

    @Override
    public void onExitRoomAdvise(String nickname) { MainWindow.onExitRoomAdvise(nickname);}

    @Override
    public void onFirstInRoomAdvise() { MainWindow.onFirstInRoomAdvise(); }

    @Override
    public void onMatchCreation(List<SimplePlayer> players, int playerTurnNumber) {
        MainWindow.onMatchCreation(players,playerTurnNumber);
    }

    @Override
    public void onInvalidMessageReceived(String msg) {
        MainWindow.onInvalidMessageReceived(msg);
    }

    @Override
    public void onBoardUpdate(SimpleBoard gameBoard) {
        MainWindow.onBoardUpdate(gameBoard);
    }

    @Override
    public void onMatchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        MainWindow.onMatchUpdate(players,gameBoard,frenzy);
    }

    @Override
    public void onRespwanRequest(List<Card> powerups) {

    }

    @Override
    public void onRespwanCompleted(SimplePlayer player, Card discardedPowerup) {

    }

    @Override
    public void onGrabbedTile(SimplePlayer player, AmmoTile grabbedTile) {

    }

    @Override
    public void onGrabbedPowerup(SimplePlayer player, Card powerup) {

    }

    @Override
    public void onGrabbableWeapons(List<Card> weapons) {

    }

    @Override
    public void onDiscardWeapon(List<Card> weapons) {

    }

    @Override
    public void onGrabbedWeapon(SimplePlayer player, Card weapon) {

    }

    @Override
    public void onReloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {

    }

    @Override
    public void onReloadableWeapons(List<Card> weapons) {

    }

    @Override
    public void onTurnActions(List<String> actions) {

    }

    @Override
    public void onTurnEnd() {

    }

    @Override
    public void onMoveAction(SimplePlayer player) {

    }

    @Override
    public void onMoveRequest(MatrixHelper matrix, String targetPlayer) {

    }

    @Override
    public void onMarkAction(String player, SimplePlayer selected, int value) {

    }

    @Override
    public void onDamageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {

    }

    @Override
    public void onDiscardedPowerup(SimplePlayer player, Card powerup) {

    }

    @Override
    public void onTurnCreation(String currentPlayer) {

    }

    @Override
    public void onSelectablePlayers(List<List<String>> selectable, SimpleTarget target) {

    }

    @Override
    public void onCanUsePowerup() {

    }

    @Override
    public void onCanStopRoutine() {

    }

    @Override
    public void onUsableWeapons(List<Card> usableWeapons) {

    }

    @Override
    public void onAvailableEffects(List<String> effects) {

    }

    @Override
    public void onPayEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {

    }

    @Override
    public void onUsedCard(Card card) {

    }

    @Override
    public void onAvailablePowerups(List<Card> powerups) {

    }

    @Override
    public void onRunCompleted(SimplePlayer player, int[] newPosition) {

    }

    @Override
    public void onRunRoutine(MatrixHelper matrix) {

    }
}