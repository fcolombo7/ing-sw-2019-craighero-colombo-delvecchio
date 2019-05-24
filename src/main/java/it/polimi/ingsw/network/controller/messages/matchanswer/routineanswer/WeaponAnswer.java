package it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.utils.Constants;

public class WeaponAnswer extends TurnRoutineAnswer {

    private static final long serialVersionUID = 8469862192467360340L;
    private Card weapon;

    public WeaponAnswer(String sender, Card weapon) {
        super(sender,Constants.WEAPON_ANSWER);
        this.weapon=weapon;
    }

    public Card getWeapon() {
        return weapon;
    }
}
