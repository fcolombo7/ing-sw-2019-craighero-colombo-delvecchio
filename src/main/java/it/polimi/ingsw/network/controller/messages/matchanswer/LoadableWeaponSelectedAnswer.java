package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.utils.Constants;

public class LoadableWeaponSelectedAnswer extends MatchAnswer {
    private static final long serialVersionUID = 9003418974244085342L;
    private Card weapon;
    public LoadableWeaponSelectedAnswer(String sender, Card weapon) {
        super(sender);
        this.weapon =weapon;
    }

    public Card getWeapon() {
        return weapon;
    }

    @Override
    public String getAnswer() {
        return Constants.LOADABLE_WEAPONS_MESSAGE;
    }
}