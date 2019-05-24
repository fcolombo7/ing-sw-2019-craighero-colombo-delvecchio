package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.utils.Constants;

public abstract class TurnRoutineAnswer extends MatchAnswer{

    private static final long serialVersionUID = -3621703218706286215L;

    private String routineAnswer;

    public TurnRoutineAnswer(String sender, String routineAnswer) {
        super(sender,Constants.TURN_ROUTINE_ANSWER);
        this.routineAnswer=routineAnswer;
    }

    public String getRoutineAnswer(){
        return routineAnswer;
    }
}
