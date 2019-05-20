package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class RespawnMessage extends MatchMessage {
    private static final long serialVersionUID = 4903474061117259731L;

    private SimplePlayer player;
    private Card discardedPowerup;

    public RespawnMessage(SimplePlayer player, Card discardedPowerup) {
        super(null);
        this.player=player;
        this.discardedPowerup=discardedPowerup;
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    public Card getDiscardedPowerup() {
        return discardedPowerup;
    }

    @Override
    public String getRequest() {
        return Constants.RESPAWN_COMPLETED_MESSAGE;
    }
}
