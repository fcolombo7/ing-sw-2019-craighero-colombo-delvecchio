package it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer;

import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.utils.Constants;

public class EffectAnswer extends TurnRoutineAnswer {
    private static final long serialVersionUID = 3800193224651001109L;
    private String effectName;

    public EffectAnswer(String sender, String effectName) {
        super(sender,Constants.EFFECT_ANSWER);
        this.effectName=effectName;
    }

    public String getEffectName() {
        return effectName;
    }

}