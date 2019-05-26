package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Powerup;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class DiscardedPowerupMessage extends MatchMessage {
    private static final long serialVersionUID = 2323009055228948884L;
    private SimplePlayer player;
    private Card powerup;

    public DiscardedPowerupMessage(Player player, Powerup powerup) {
        super(null, Constants.DISCARDED_POWERUP_MESSAGE);
        this.player=new SimplePlayer(player);
        this.powerup=new Card(powerup);
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    public Card getPowerup() {
        return powerup;
    }
}
