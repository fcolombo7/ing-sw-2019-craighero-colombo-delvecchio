package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.utils.Costants;

public class RunRoutineAnswer extends TurnRoutineAnswer {
    private static final long serialVersionUID = 1399452844399037037L;

    private int[] newPosition;

    public RunRoutineAnswer(String sender, int[] newPosition) {
        super(sender);
        if(newPosition.length!=2)throw new IllegalArgumentException("Invalid position indexes.");
        this.newPosition=new int[]{newPosition[0],newPosition[1]};
    }

    public int[] getNewPosition() {
        return new int[]{newPosition[0],newPosition[1]};
    }

    @Override
    public String getRoutineAnswer() {
        return Costants.RUN_ROUTINE_ANSWER;
    }
}
