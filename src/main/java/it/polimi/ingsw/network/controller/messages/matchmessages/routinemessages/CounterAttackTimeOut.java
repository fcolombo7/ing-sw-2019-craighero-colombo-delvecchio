package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

public class CounterAttackTimeOut extends TurnRoutineMessage {
    private static final long serialVersionUID = 7981461603873091950L;

    public CounterAttackTimeOut(String nickname) {
        super(nickname, Constants.COUNTER_ATTACK_TIMEOUT);
    }
}
