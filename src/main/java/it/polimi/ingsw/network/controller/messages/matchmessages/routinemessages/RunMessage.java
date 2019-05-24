package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.MatrixHelper;
import it.polimi.ingsw.utils.Constants;

public class RunMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = 4466037763772949493L;
    private MatrixHelper matrix;

    public RunMessage(String recipient, MatrixHelper matrix) {
        super(recipient,Constants.RUN_ROUTINE_MESSAGE);
        this.matrix=matrix;
    }

    public MatrixHelper getMatrix() {
        return matrix;
    }

}
