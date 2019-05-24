package it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.utils.Constants;

public class LoadableWeaponSelectedAnswer extends TurnRoutineAnswer {
    private static final long serialVersionUID = 9003418974244085342L;
    private Card weapon;
    public LoadableWeaponSelectedAnswer(String sender, Card weapon) {
        super(sender,Constants.LOADABLE_WEAPON_SELECTED);
        this.weapon =weapon;
    }

    public Card getWeapon() {
        return weapon;
    }

}