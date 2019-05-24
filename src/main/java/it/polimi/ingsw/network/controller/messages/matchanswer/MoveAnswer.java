package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.utils.Constants;

public class MoveAnswer extends MatchAnswer{
    private static final long serialVersionUID = -6657309365124541167L;

    private int[] newPosition;
    private String target;
    public MoveAnswer(String sender,String target, int[] newPosition) {
        super(sender,Constants.EFFECT_MOVE_ANSWER);
        this.target=target;
        if(newPosition.length!=2)throw new IllegalArgumentException("Invalid position indexes.");
        this.newPosition=new int[]{newPosition[0],newPosition[1]};
    }

    public String getTarget() {
        return target;
    }

    public int[] getNewPosition() {
        return new int[]{newPosition[0],newPosition[1]};
    }

}