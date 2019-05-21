package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.utils.Constants;

public class ActionSelectedAnswer extends MatchAnswer{
    private static final long serialVersionUID = -4358211396388669007L;

    private String selection;
    public ActionSelectedAnswer(String sender, String selection) {
        super(sender);
        this.selection=selection;
    }

    public String getSelection() {
        return selection;
    }

    @Override
    public String getAnswer() {
        return Constants.ACTION_SELECTED;
    }
}
