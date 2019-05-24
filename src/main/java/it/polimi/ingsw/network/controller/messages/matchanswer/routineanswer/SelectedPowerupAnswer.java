package it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.utils.Constants;

public class SelectedPowerupAnswer extends TurnRoutineAnswer {
    private static final long serialVersionUID = -4774728423359818012L;

    private Card powerup;
    public SelectedPowerupAnswer(String sender, Card powerup) {
        super(sender,Constants.POWERUP_ANSWER);
        this.powerup=powerup;
    }

    public Card getPowerup() {
        return powerup;
    }

}
