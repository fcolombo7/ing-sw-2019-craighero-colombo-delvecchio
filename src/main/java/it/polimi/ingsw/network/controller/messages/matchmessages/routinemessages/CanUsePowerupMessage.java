package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

public class CanUsePowerupMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = 3729068918027149808L;

    public CanUsePowerupMessage(String recipient) {
        super(recipient, Constants.CAN_USE_POWERUP);
    }
}
