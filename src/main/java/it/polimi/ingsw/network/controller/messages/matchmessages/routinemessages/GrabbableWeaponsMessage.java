package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GrabbableWeaponsMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = -1244443483410204455L;
    private List<Card> weapons;

    public GrabbableWeaponsMessage(String recipient, List<Card> weapons) {
        super(recipient, Constants.GRABBABLE_WEAPONS_MESSAGE);
        this.weapons=new ArrayList<>(weapons);
    }

    public List<Card> getWeapons() {
        return weapons;
    }
}
