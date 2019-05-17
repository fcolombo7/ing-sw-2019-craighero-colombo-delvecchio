package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.utils.MatrixHelper;
import it.polimi.ingsw.utils.Costants;

public class RunRoutineMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = 4466037763772949493L;
    private MatrixHelper matrix;

    public RunRoutineMessage(String recipient, MatrixHelper matrix) {
        super(recipient);
        this.matrix=matrix;
    }

    public MatrixHelper getMatrix() {
        return matrix;
    }

    @Override
    public String getRoutineRequest() {
        return Costants.RUN_ROUTINE_MESSAGE;
    }
}
