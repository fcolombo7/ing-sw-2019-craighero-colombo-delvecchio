package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class RespawnMessage extends MatchMessage {
    private static final long serialVersionUID = 4903474061117259731L;

    private SimplePlayer player;
    private Card discardedPowerup;
    private Color powerupColor;

    public RespawnMessage(SimplePlayer player, Card discardedPowerup, Color pColor) {
        super(null,Constants.RESPAWN_COMPLETED_MESSAGE);
        this.player=player;
        this.discardedPowerup=discardedPowerup;
        this.powerupColor=pColor;
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    public Card getDiscardedPowerup() {
        return discardedPowerup;
    }

    public Color getPowerupColor() {
        return powerupColor;
    }
}
