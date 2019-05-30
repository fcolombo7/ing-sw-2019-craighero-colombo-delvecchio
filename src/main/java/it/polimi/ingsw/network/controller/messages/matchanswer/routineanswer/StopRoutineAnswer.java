package it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer;

import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.utils.Constants;

public class StopRoutineAnswer extends TurnRoutineAnswer {
    private static final long serialVersionUID = -254664955310026404L;
    private boolean stop;

    public StopRoutineAnswer(String sender, boolean stop) {
        super(sender,Constants.STOP_ROUTINE_ANSWER);
        this.stop=stop;
    }

    public boolean wishStop() {
        return stop;
    }

}
