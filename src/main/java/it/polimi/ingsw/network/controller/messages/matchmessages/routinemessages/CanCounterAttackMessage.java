package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

public class CanCounterAttackMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = -919440068296620083L;

    public CanCounterAttackMessage(String nickname) {
        super(nickname, Constants.CAN_COUNTER_ATTACK);
    }
}
