package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.utils.Constants;

public class PowerupSelectedAnswer extends MatchAnswer {
    private static final long serialVersionUID = -4774728423359818012L;

    private Card powerup;
    public PowerupSelectedAnswer(String sender, Card powerup) {
        super(sender);
        this.powerup=powerup;
    }

    public Card getPowerup() {
        return powerup;
    }

    @Override
    public String getAnswer() {
        return Constants.POWERUP_SELECTED;
    }
}
