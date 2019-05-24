package it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer;

import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.utils.Constants;

public class UsePowerupAnswer extends TurnRoutineAnswer {
    private static final long serialVersionUID = 7950789210727598814L;
    private boolean use;

    public UsePowerupAnswer(String sender, boolean use) {
        super(sender, Constants.USE_POWERUP_ANSWER);
        this.use=use;
    }

    public boolean wishUseIt() {
        return use;
    }
}
