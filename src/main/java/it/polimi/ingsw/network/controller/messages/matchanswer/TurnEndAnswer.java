package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.utils.Constants;

public class TurnEndAnswer extends MatchAnswer{
    private static final long serialVersionUID = -829578163101090613L;

    public TurnEndAnswer(String sender) {
        super(sender, Constants.TURN_END_ANSWER);
    }
}
