package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

public class FullOfPowerupsMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = 1256103086285181736L;
    public FullOfPowerupsMessage(String recipient) {
        super(recipient, Constants.FULL_OF_POWERUP);
    }
}
