package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ReloadableWeaponsMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = -5997612003605591407L;

    private List<Card> weapons;

    public ReloadableWeaponsMessage(String recipient, List<Card> weapons) {
        super(recipient,Constants.RELOADABLE_WEAPONS_MESSAGE);
        this.weapons=new ArrayList<>(weapons);
    }

    public List<Card> getWeapons() {
        return new ArrayList<>(weapons);
    }

}
