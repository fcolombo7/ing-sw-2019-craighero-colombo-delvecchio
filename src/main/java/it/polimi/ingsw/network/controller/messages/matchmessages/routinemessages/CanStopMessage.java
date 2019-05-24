package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

public class CanStopMessage extends TurnRoutineMessage {

    private static final long serialVersionUID = -4352915245951560935L;

    public CanStopMessage(String recipient) {
        super(recipient,Constants.CAN_STOP_ROUTINE);
    }

}
