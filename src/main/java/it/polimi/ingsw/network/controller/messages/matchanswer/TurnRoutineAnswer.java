package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.utils.Constants;

public abstract class TurnRoutineAnswer extends MatchAnswer{

    private static final long serialVersionUID = -3621703218706286215L;

    public TurnRoutineAnswer(String sender) {
        super(sender);
    }

    @Override
    public String getAnswer() {
        return Constants.TURN_ROUTINE_ANSWER;
    }

    public abstract String getRoutineAnswer();
}
