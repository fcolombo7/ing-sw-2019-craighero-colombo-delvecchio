package it.polimi.ingsw.network.controller.messages.matchanswer;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.utils.Constants;

public class RespawnAnswer extends MatchAnswer {
    private static final long serialVersionUID = 4203506623190676576L;

    private Card powerup;
    public RespawnAnswer(String sender,Card powerup){
        super(sender,Constants.RESPAWN_ANSWER);
        this.powerup=powerup;
    }

    public Card getPowerup() {
        return powerup;
    }

}
