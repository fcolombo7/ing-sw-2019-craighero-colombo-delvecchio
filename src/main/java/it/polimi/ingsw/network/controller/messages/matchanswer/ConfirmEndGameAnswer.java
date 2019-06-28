package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.utils.Constants;

public class ConfirmEndGameAnswer extends MatchAnswer{
    private static final long serialVersionUID = -2342523698372422384L;

    public ConfirmEndGameAnswer(String sender) {
        super(sender, Constants.CONFIRM_END_GAME);
    }
}
