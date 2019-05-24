package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AvailableWeaponsMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = -212894878257900495L;
    private final List<Card> availableWeapons;

    public AvailableWeaponsMessage(String recipient, List<Card> availableWeapons) {
        super(recipient,Constants.AVAILABLE_WEAPONS_MESSAGE);
        this.availableWeapons=new ArrayList<>(availableWeapons);
    }

    public List<Card> getAvailableWeapons() {
        return new ArrayList<>(availableWeapons);
    }

}
