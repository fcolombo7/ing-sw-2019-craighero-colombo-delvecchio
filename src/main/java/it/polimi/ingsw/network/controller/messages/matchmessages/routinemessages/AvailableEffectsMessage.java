package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.model.Effect;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AvailableEffectsMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = 1294002369241162357L;
    private List<String> effects;
    public AvailableEffectsMessage(String recipient, List<Effect> effects) {
        super(recipient,Constants.AVAILABLE_EFFECT_MESSAGE);
        this.effects=new ArrayList<>();
        for(Effect effect:effects)
            this.effects.add(effect.getName());
    }

    public List<String> getEffects() {
        return new ArrayList<>(effects);
    }

}
