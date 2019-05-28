package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class PayEffectMessage extends MatchMessage {
    private static final long serialVersionUID = -5365754129259274433L;
    private SimplePlayer player;
    private List<Color> usedAmmo;
    private List<Card> discardedPowerups;

    public PayEffectMessage(SimplePlayer player, List<Color> usedAmmo, List<Card> discardedPowerups) {
        super(null, Constants.PAY_EFFECT_MESSAGE);
        this.player=player;
        this.usedAmmo=new ArrayList<>(usedAmmo);
        this.discardedPowerups=new ArrayList<>(discardedPowerups);
    }

    public List<Card> getDiscardedPowerups() {
        return new ArrayList<>(discardedPowerups);
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    public List<Color> getUsedAmmo() {
        return new ArrayList<>(usedAmmo);
    }

}
