package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.MatrixHelper;

public class MoveRequestMessage extends MatchMessage {
    private static final long serialVersionUID = 4919549472641984211L;
    private String target;
    private MatrixHelper matrix;

    public MoveRequestMessage(String recipient, String target, MatrixHelper matrix) {
        super(recipient,Constants.EFFECT_MOVE_REQUEST_MESSAGE);
        this.matrix=matrix;
        this.target=target;
    }

    public MatrixHelper getMatrix() {
        return matrix;
    }

    public String getTarget() {
        return target;
    }

}
