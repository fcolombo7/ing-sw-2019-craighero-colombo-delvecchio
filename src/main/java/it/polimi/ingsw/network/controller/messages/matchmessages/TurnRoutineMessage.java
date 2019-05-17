package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.utils.Costants;

public abstract class TurnRoutineMessage extends MatchMessage {

    private static final long serialVersionUID = -168639418682449484L;

    public TurnRoutineMessage(String recipient) {
        super(recipient);
    }

    @Override
    public String getRequest() {
        return Costants.TURN_ROUTINE_MESSAGE;
    }

    public abstract String getRoutineRequest();
}
