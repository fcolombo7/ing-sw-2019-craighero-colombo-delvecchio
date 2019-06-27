package it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer;

import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.utils.Constants;

public class CounterAttackAnswer extends TurnRoutineAnswer {
    private boolean use;

    public CounterAttackAnswer(String sender, boolean use) {
        super(sender, Constants.COUNTER_ATTACK_ANSWER);
        this.use = use;
    }

    public boolean counterAttack() {
        return use;
    }
}