package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class RespawnRequestMessage extends MatchMessage {

    private static final long serialVersionUID = 5048593416397821851L;

    private List<Card> powerups;
    private List<Color> colors;

    public RespawnRequestMessage(String recipient, List<Card> powerups, List<Color> colors) {
        super(recipient,Constants.RESPAWN_REQUEST_MESSAGE);
        this.powerups=new ArrayList<>(powerups.size());
        for (Card card:powerups)
            this.powerups.add(new Card(card));
        this.colors=new ArrayList<>(colors);
    }

    public List<Card> getPowerups() {
        return new ArrayList<>(powerups);
    }

    public List<Color> getColors() {
        return colors;
    }
}