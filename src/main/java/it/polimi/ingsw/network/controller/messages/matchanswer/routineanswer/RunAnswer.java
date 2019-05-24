package it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer;

import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.utils.Constants;

public class RunAnswer extends TurnRoutineAnswer {
    private static final long serialVersionUID = 1399452844399037037L;

    private int[] newPosition;

    public RunAnswer(String sender, int[] newPosition) {
        super(sender,Constants.RUN_ROUTINE_ANSWER);
        if(newPosition.length!=2)throw new IllegalArgumentException("Invalid position indexes.");
        this.newPosition=new int[]{newPosition[0],newPosition[1]};
    }

    public int[] getNewPosition() {
        return new int[]{newPosition[0],newPosition[1]};
    }

}
