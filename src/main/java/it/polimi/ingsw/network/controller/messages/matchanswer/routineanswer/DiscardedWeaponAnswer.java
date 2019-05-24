package it.polimi.ingsw.network.controller.messages.matchanswer.routineanswer;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.matchanswer.TurnRoutineAnswer;
import it.polimi.ingsw.utils.Constants;

public class DiscardedWeaponAnswer extends TurnRoutineAnswer {

    private static final long serialVersionUID = -5915886112220296431L;
    private Card weapon;

    public DiscardedWeaponAnswer(String sender, Card weapon) {
        super(sender, Constants.DISCARDED_WEAPON_ANSWER);
        this.weapon=weapon;
    }

    public Card getWeapon() {
        return weapon;
    }
}