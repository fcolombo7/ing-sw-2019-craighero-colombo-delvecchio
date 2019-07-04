package it.polimi.ingsw.network.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages.*;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;

import java.util.HashMap;
import java.util.Map;

public class RemoteView extends View{

    private ClientConnection clientConnection;
    private Map<String, MessageSender> senderMap;

    public RemoteView(Player player, ClientConnection client) {
        super(player);
        this.clientConnection = client;
        initMap();
    }

    @Override
    protected void show(MatchMessage message) {
        if(message.getRequest().equalsIgnoreCase(Constants.TURN_ROUTINE_MESSAGE)) {
            Logger.log("Sending a "+((TurnRoutineMessage) message).getRoutineRequest()+" to "+getPlayer().getNickname());
            senderMap.get(((TurnRoutineMessage) message).getRoutineRequest()).send(message);
        }
        else {
            Logger.log("Sending a "+message.getRequest()+" to "+getPlayer().getNickname());
            senderMap.get(message.getRequest()).send(message);
        }
    }

    @Override
    public void update(MatchMessage message) {
        String recipient=message.getRecipient();
        if(recipient==null||recipient.equalsIgnoreCase(getPlayer().getNickname()))
            show(message);
    }

    private void initMap() {
        senderMap=new HashMap<>();
        senderMap.put(Constants.CREATION_MESSAGE,this::matchCreation);
        senderMap.put(Constants.INVALID_ANSWER,this::invalidAnswer);
        senderMap.put(Constants.BOARD_UPDATE_MESSAGE,this::boardUpdate);
        senderMap.put(Constants.UPDATE_MESSAGE,this::matchUpdate);
        senderMap.put(Constants.RECOVERING_PLAYER,this::recoveringPlayer);
        senderMap.put(Constants.RESPAWN_REQUEST_MESSAGE,this::respwanRequest);
        senderMap.put(Constants.RESPAWN_COMPLETED_MESSAGE,this::respwanCompleted);

        senderMap.put(Constants.TURN_CREATION_MESSAGE,this::turnCreation);
        senderMap.put(Constants.TURN_END_MESSAGE,this::turnEnd);
        senderMap.put(Constants.TURN_AVAILABLE_ACTIONS,this::turnActions);
        senderMap.put(Constants.RELOADABLE_WEAPONS_MESSAGE,this::reloadableWeapons);
        senderMap.put(Constants.RELOAD_COMPLETED,this::reloadedWeapon);
        senderMap.put(Constants.GRABBED_WEAPON_MESSAGE,this::grabbedWeapon);
        senderMap.put(Constants.DISCARD_WEAPON_MESSAGE,this::discardWeapon);
        senderMap.put(Constants.GRABBABLE_WEAPONS_MESSAGE,this::grabbableWeapons);
        senderMap.put(Constants.GRABBED_POWERUP,this::grabbedPowerup);
        senderMap.put(Constants.GRABBED_TILE_MESSAGE,this::grabbedTile);
        senderMap.put(Constants.FULL_OF_POWERUP,this::fullOfPowerup);

        senderMap.put(Constants.RUN_ROUTINE_MESSAGE,this::runRoutine);
        senderMap.put(Constants.RUN_COMPLETED,this::runCompleted);
        senderMap.put(Constants.AVAILABLE_POWERUPS_MESSAGE,this::availablePowerups);
        senderMap.put(Constants.USED_CARD_MESSAGE,this::usedCard);
        senderMap.put(Constants.PAY_EFFECT_MESSAGE,this::payEffect);
        senderMap.put(Constants.AVAILABLE_EFFECTS_MESSAGE,this::availableEffects);
        senderMap.put(Constants.USABLE_WEAPONS_MESSAGE,this::usableWeapons);
        senderMap.put(Constants.CAN_STOP_ROUTINE,this::canStopRoutine);
        senderMap.put(Constants.CAN_USE_POWERUP,this::canUsePowerup);
        senderMap.put(Constants.SELECTABLE_PLAYERS_MESSAGE,this::selectablePlayers);
        senderMap.put(Constants.DISCARDED_POWERUP_MESSAGE,this::discardedPowerup);

        senderMap.put(Constants.EFFECT_DAMAGE_MESSAGE,this::damageAction);
        senderMap.put(Constants.EFFECT_MARK_MESSAGE,this::markAction);
        senderMap.put(Constants.EFFECT_MOVE_REQUEST_MESSAGE,this::moveRequest);
        senderMap.put(Constants.EFFECT_MOVE_MESSAGE,this::moveAction);

        senderMap.put(Constants.CAN_COUNTER_ATTACK,this::canCounterAttack);
        senderMap.put(Constants.COUNTER_ATTACK_COMPLETED,this::counterAttack);
        senderMap.put(Constants.COUNTER_ATTACK_TIMEOUT,this::counterAttackTimeout);

        senderMap.put(Constants.LEADERBOARD_MESSAGE,this::leaderboardMessage);
        senderMap.put(Constants.GAME_END_MESSAGE,this::gameEndMessage);
    }

    private void leaderboardMessage(MatchMessage message) {
        LeaderboardMessage msg=(LeaderboardMessage) message;
        clientConnection.sendLeaderboard(msg.getNicknames(),msg.getPoints());
    }

    private void gameEndMessage(MatchMessage message) {
        GameEndMessage msg=(GameEndMessage) message;
        clientConnection.gameEnd(msg.getPlayers());
    }

    private void counterAttackTimeout(MatchMessage message) {
        CounterAttackTimeOut msg=(CounterAttackTimeOut) message;
        clientConnection.counterAttackTimeOut();
    }

    private void counterAttack(MatchMessage message) {
        CounterAttackMessage msg=(CounterAttackMessage) message;
        clientConnection.counterAttack(msg.getCurrentPlayer(),msg.getPlayer(),msg.getPowerup());
    }

    private void canCounterAttack(MatchMessage message) {
        CanCounterAttackMessage msg=(CanCounterAttackMessage) message;
        clientConnection.canCounterAttack();
    }

    private void fullOfPowerup(MatchMessage message) {
        FullOfPowerupsMessage msg=(FullOfPowerupsMessage) message;
        clientConnection.fullOfPowerup();
    }

    private void moveAction(MatchMessage message) {
        MoveMessage msg=(MoveMessage)message;
        clientConnection.moveAction(msg.getPlayer());
    }

    private void moveRequest(MatchMessage message) {
        MoveRequestMessage msg=(MoveRequestMessage)message;
        clientConnection.moveRequest(msg.getMatrix(),msg.getTargetPlayer());
    }

    private void markAction(MatchMessage message) {
        MarkMessage msg=(MarkMessage)message;
        clientConnection.markAction(msg.getPlayer(),msg.getSelected(),msg.getValue());
    }

    private void damageAction(MatchMessage message) {
        DamageMessage msg=(DamageMessage)message;
        clientConnection.damageAction(msg.getPlayer(),msg.getSelected(),msg.getDamageValue(),msg.getConvertedMarks());
    }

    private void discardedPowerup(MatchMessage message) {
        DiscardedPowerupMessage msg=(DiscardedPowerupMessage)message;
        clientConnection.discardedPowerup(msg.getPlayer(),msg.getPowerup());
    }

    private void turnCreation(MatchMessage message) {
        TurnCreationMessage msg=(TurnCreationMessage)message;
        clientConnection.turnCreation(msg.getCurrentPlayer());
    }

    private void selectablePlayers(MatchMessage message) {
        SelectablePlayersMessage msg=(SelectablePlayersMessage)message;
        clientConnection.selectablePlayers(msg.getSelectable(),msg.getTarget());
    }

    private void canUsePowerup(MatchMessage message) {
        clientConnection.canUsePowerup();
    }

    private void canStopRoutine(MatchMessage message) {
        clientConnection.canStopRoutine();
    }

    private void usableWeapons(MatchMessage message) {
        UsableWeaponsMessage msg=(UsableWeaponsMessage)message;
        clientConnection.usableWeapons(msg.getUsableWeapons());
    }

    private void availableEffects(MatchMessage message) {
        AvailableEffectsMessage msg=(AvailableEffectsMessage)message;
        clientConnection.availableEffects(msg.getEffects());
    }

    private void payEffect(MatchMessage message) {
        PayEffectMessage msg=(PayEffectMessage)message;
        clientConnection.payEffect(msg.getPlayer(),msg.getDiscardedPowerups(),msg.getUsedAmmo());
    }

    private void usedCard(MatchMessage message) {
        UsedCardMessage msg=(UsedCardMessage)message;
        clientConnection.usedCard(msg.getCard());
    }

    private void availablePowerups(MatchMessage message) {
        AvailablePowerupsMessage msg=(AvailablePowerupsMessage)message;
        clientConnection.availablePowerups(msg.getPowerups(),msg.getColors());
    }

    private void runCompleted(MatchMessage message) {
        RunCompleted msg=(RunCompleted)message;
        clientConnection.runCompleted(msg.getPlayer(),msg.getNewPosition());
    }

    private void runRoutine(MatchMessage message) {
        RunMessage msg=(RunMessage)message;
        clientConnection.runRoutine(msg.getMatrix());
    }

    private void grabbedTile(MatchMessage message) {
        GrabbedAmmoTileMessage msg=(GrabbedAmmoTileMessage)message;
        clientConnection.grabbedTile(msg.getPlayer(),msg.getGrabbedTile());
    }

    private void grabbedPowerup(MatchMessage message) {
        GrabbedPowerupMessage msg=(GrabbedPowerupMessage)message;
        clientConnection.grabbedPowerup(msg.getPlayer(),msg.getPowerup(),msg.getColor());
    }

    private void grabbableWeapons(MatchMessage message) {
        GrabbableWeaponsMessage msg=(GrabbableWeaponsMessage)message;
        clientConnection.grabbableWeapons(msg.getWeapons());
    }

    private void discardWeapon(MatchMessage message) {
        DiscardWeaponMessage msg=(DiscardWeaponMessage)message;
        clientConnection.discardWeapon(msg.getWeapons());
    }

    private void grabbedWeapon(MatchMessage message) {
        GrabbedWeaponMessage msg=(GrabbedWeaponMessage)message;
        clientConnection.grabbedWeapon(msg.getPlayer(),msg.getWeapon());
    }

    private void reloadedWeapon(MatchMessage message) {
        WeaponReloadedMessage msg=(WeaponReloadedMessage)message;
        clientConnection.reloadedWeapon(msg.getPlayer(),msg.getWeapon(),msg.getDiscardedPowerups(),msg.getTotalCost());
    }

    private void reloadableWeapons(MatchMessage message) {
        ReloadableWeaponsMessage msg=(ReloadableWeaponsMessage)message;
        clientConnection.reloadableWeapons(msg.getWeapons());
    }

    private void turnActions(MatchMessage message) {
        TurnActionsMessage msg=(TurnActionsMessage)message;
        clientConnection.turnActions(msg.getActions());
    }

    private void turnEnd(MatchMessage message) {
        clientConnection.turnEnd();
    }

    private void matchCreation(MatchMessage message){
        MatchCreationMessage msg=(MatchCreationMessage)message;
        clientConnection.matchCreation(msg.getPlayers(),msg.getPlayerTurnNumber());
    }

    private void invalidAnswer(MatchMessage message){
        InvalidAnswerMessage msg=(InvalidAnswerMessage)message;
        clientConnection.invalidMessageReceived(msg.getMsg());
    }

    private void boardUpdate(MatchMessage message){
        BoardUpdateMessage msg=(BoardUpdateMessage)message;
        clientConnection.boardUpdate(msg.getGameBoard());
    }

    private void matchUpdate(MatchMessage message){
        MatchUpdateMessage msg=(MatchUpdateMessage)message;
        clientConnection.matchUpdate(msg.getPlayers(),msg.getGameBoard(),msg.isFrenzy());
    }

    private void recoveringPlayer(MatchMessage message){
        RecoveringPlayerMessage msg=(RecoveringPlayerMessage)message;
        clientConnection.recoveringPlayer(msg.getPlayers(),msg.getGameBoard(),msg.isFrenzy());
    }

    private void respwanRequest(MatchMessage message){
        RespawnRequestMessage msg=(RespawnRequestMessage)message;
        clientConnection.respwanRequest(msg.getPowerups(),msg.getColors());
    }

    private void respwanCompleted(MatchMessage message){
        RespawnMessage msg=(RespawnMessage)message;
        clientConnection.respwanCompleted(msg.getPlayer(),msg.getDiscardedPowerup(),msg.getPowerupColor());
    }

    @FunctionalInterface
    private interface MessageSender{
        void send(MatchMessage message);
    }
}
