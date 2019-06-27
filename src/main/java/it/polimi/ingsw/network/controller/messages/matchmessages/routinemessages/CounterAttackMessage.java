package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

public class CounterAttackMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = 2162917125916427277L;

    private SimplePlayer currentPlayer, player;
    private Card powerup;
    public CounterAttackMessage(SimplePlayer currentPlayer, SimplePlayer player, Card powerup) {
        super(null, Constants.COUNTER_ATTACK_COMPLETED);
        this.currentPlayer=currentPlayer;
        this.player=player;
        this.powerup=powerup;
    }

    public SimplePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    public Card getPowerup() {
        return powerup;
    }
}
