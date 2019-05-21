package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.utils.Constants;

public class SelectedPowerupMessage extends MatchMessage {

    private static final long serialVersionUID = -6349210877777002611L;
    private String player;
    private Card powerup;

    public SelectedPowerupMessage(String player, Card powerup) {
        super(null);
        this.player=player;
        this.powerup=new Card(powerup);
    }

    public String getPlayer() {
        return player;
    }

    public Card getPowerup() {
        return powerup;
    }

    @Override
    public String getRequest() {
        return Constants.SELECTED_POWERUP;
    }
}
