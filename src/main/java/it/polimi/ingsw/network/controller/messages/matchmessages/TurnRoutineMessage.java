package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.utils.Constants;

public abstract class TurnRoutineMessage extends MatchMessage {

    private static final long serialVersionUID = -168639418682449484L;

    private String routineRequest;

    public TurnRoutineMessage(String recipient, String routineRequest) {
        super(recipient,Constants.TURN_ROUTINE_MESSAGE);
        this.routineRequest=routineRequest;
    }

    public String getRoutineRequest(){
        return routineRequest;
    }
}
