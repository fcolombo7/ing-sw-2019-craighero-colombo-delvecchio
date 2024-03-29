package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class WeaponReloadedMessage extends MatchMessage {
    private static final long serialVersionUID = 5737998367182107046L;

    private SimplePlayer player;
    private List<Card> discardedPowerups;
    private List<Color> totalCost;
    private Card weapon;

    public WeaponReloadedMessage(SimplePlayer player, Card weapon, List<Card> discardedPowerup, List<Color> totalCost) {
        super(null,Constants.RELOAD_COMPLETED);
        this.player=player;
        this.weapon=new Card(weapon);
        this.totalCost=new ArrayList<>(totalCost);
        this.discardedPowerups=new ArrayList<>(discardedPowerup);
    }

    public Card getWeapon() {
        return weapon;
    }

    public List<Color> getTotalCost() {
        return totalCost;
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    public List<Card> getDiscardedPowerups() {
        return new ArrayList<>(discardedPowerups);
    }

}
