package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class RespawnRequestMessage extends MatchMessage {

    private static final long serialVersionUID = 5048593416397821851L;

    private List<Card> powerups;

    public RespawnRequestMessage(String recipient, List<Card> powerups) {
        super(recipient,Constants.RESPAWN_REQUEST_MESSAGE);
        this.powerups=new ArrayList<>(powerups.size());
        for (Card card:powerups)
            powerups.add(new Card(card));
    }

    public List<Card> getPowerups() {
        return new ArrayList<>(powerups);
    }

}