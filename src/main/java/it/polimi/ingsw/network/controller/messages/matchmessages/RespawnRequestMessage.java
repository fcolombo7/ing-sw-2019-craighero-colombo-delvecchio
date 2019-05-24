package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class RespawnRequestMessage extends MatchMessage {

    private static final long serialVersionUID = 5048593416397821851L;

    private List<Card> drawnPowerups;

    public RespawnRequestMessage(String recipient, List<Card> drawedPowerups) {
        super(recipient,Constants.RESPAWN_REQUEST_MESSAGE);
        this.drawnPowerups=new ArrayList<>(drawedPowerups);
    }

    public List<Card> getDrawedPowerups() {
        return new ArrayList<>(drawnPowerups);
    }

}