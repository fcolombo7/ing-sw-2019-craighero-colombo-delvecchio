package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;

import java.util.ArrayList;
import java.util.List;

public class WeaponReloadMessage extends MatchMessage {
    private static final long serialVersionUID = 5737998367182107046L;

    private SimplePlayer player;
    private List<Card> discardedPowerups;

    public WeaponReloadMessage(SimplePlayer player, List<Card> discardedPowerup) {
        super(null);
        this.player=player;
        this.discardedPowerups=new ArrayList<>(discardedPowerup);
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    public List<Card> getDiscardedPowerups() {
        return new ArrayList<>(discardedPowerups);
    }

    @Override
    public String getRequest() {
        return null;
    }
}
