package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class UsableWeaponsMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = -212894878257900495L;
    private final List<Card> usableWeapons;

    public UsableWeaponsMessage(String recipient, List<Card> usableWeapons) {
        super(recipient,Constants.USABLE_WEAPONS_MESSAGE);
        this.usableWeapons =new ArrayList<>(usableWeapons);
    }

    public List<Card> getUsableWeapons() {
        return new ArrayList<>(usableWeapons);
    }

}
