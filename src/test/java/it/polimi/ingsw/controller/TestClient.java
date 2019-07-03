package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.Controller;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.utils.MatrixHelper;

import java.util.ArrayDeque;
import java.util.List;

class TestClient extends ClientConnection {
    private ArrayDeque<String> collector;
    private Controller controller;
    TestClient(ArrayDeque<String> collector){
        this.collector=collector;
    }
    void setCollector(Controller controller){
        this.controller=controller;
    }
    @Override
    public void closeConnection() {

    }

    @Override
    public void joinRoomAdvise(String nickname) {
        collector.push("JOIN");
    }

    @Override
    public void exitRoomAdvise(String nickname) {
        collector.push("EXIT");
    }

    @Override
    public void firstInRoomAdvise() {
        collector.push("FIRST");
    }

    @Override
    public void keepAlive() {
    }

    @Override
    public void matchCreation(List<SimplePlayer> players, int playerTurnNumber) {
        collector.push("MATCH_CREATION");
    }

    @Override
    public void invalidMessageReceived(String msg) {

    }

    @Override
    public void boardUpdate(SimpleBoard gameBoard) {

    }

    @Override
    public void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {

    }

    @Override
    public void recoveringPlayer(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {

    }

    @Override
    public void respwanRequest(List<Card> powerups,List<Color> colors) {
        collector.push("RESPAWN_REQUEST");
        controller.respawnPlayer(getNickname(),powerups.get(0));
    }

    @Override
    public void respwanCompleted(SimplePlayer player, Card discardedPowerup, Color color) {
        collector.push("RESPAWN_COMPLETED");
    }

    @Override
    public void grabbedTile(SimplePlayer player, AmmoTile grabbedTile) {

    }

    @Override
    public void grabbedPowerup(SimplePlayer player, Card powerup, Color color) {

    }

    @Override
    public void grabbableWeapons(List<Card> weapons) {

    }

    @Override
    public void discardWeapon(List<Card> weapons) {

    }

    @Override
    public void grabbedWeapon(SimplePlayer player, Card weapon) {

    }

    @Override
    public void reloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {

    }

    @Override
    public void reloadableWeapons(List<Card> weapons) {

    }

    @Override
    public void turnActions(List<String> actions) {
        collector.push("TURN_ACTIONS");
    }

    @Override
    public void turnEnd() {
        collector.push("TURN_END");
    }

    @Override
    public void moveAction(SimplePlayer player) {

    }

    @Override
    public void moveRequest(MatrixHelper matrix, String targetPlayer) {

    }

    @Override
    public void markAction(String player, SimplePlayer selected, int value) {

    }

    @Override
    public void damageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {

    }

    @Override
    public void discardedPowerup(SimplePlayer player, Card powerup) {
        collector.push("DISCARDED_POWERUP");
    }

    @Override
    public void fullOfPowerup() {

    }

    @Override
    public void turnCreation(String currentPlayer) {
        collector.push("TURN_CREATION");
    }

    @Override
    public void selectablePlayers(List<List<String>> selectable, SimpleTarget target) {

    }

    @Override
    public void canUsePowerup() {

    }

    @Override
    public void canStopRoutine() {

    }

    @Override
    public void usableWeapons(List<Card> usableWeapons) {

    }

    @Override
    public void availableEffects(List<String> effects) {

    }

    @Override
    public void payEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {

    }

    @Override
    public void usedCard(Card card) {

    }

    @Override
    public void availablePowerups(List<Card> powerups, List<Color> colors) {

    }

    @Override
    public void runCompleted(SimplePlayer player, int[] newPosition) {
        collector.push("RUN_COMPLETED");
    }

    @Override
    public void runRoutine(MatrixHelper matrix) {
        System.out.println("Run routine...");
        for(int i=0;i<matrix.getRowLength();i++){
            for(int j=0;j<matrix.getColLength();j++) {
                if (matrix.toBooleanMatrix()[i][j]) {
                    controller.runAction(new int[]{i, j});
                    return;
                }
            }
        }
    }

    @Override
    public void recoverAdvise(String nickname) {

    }

    @Override
    public void canCounterAttack() {

    }

    @Override
    public void counterAttack(SimplePlayer currentPlayer, SimplePlayer player, Card powerup) {

    }

    @Override
    public void counterAttackTimeOut() {

    }

    @Override
    public void notifyDisconnetion() {

    }

    @Override
    public void gameEnd(List<SimplePlayer> players) {

    }

    @Override
    public void sendLeaderboard(List<String> nicknames, List<Integer> points) {

    }
}