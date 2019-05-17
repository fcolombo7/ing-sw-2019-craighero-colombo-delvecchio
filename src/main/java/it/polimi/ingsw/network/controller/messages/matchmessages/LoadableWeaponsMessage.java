package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.utils.Costants;

import java.util.ArrayList;
import java.util.List;

public class LoadableWeaponsMessage extends MatchMessage {
    private static final long serialVersionUID = -5997612003605591407L;

    private List<Card> weapons;

    public LoadableWeaponsMessage(String recipient, List<Card> weapons) {
        super(recipient);
        this.weapons=new ArrayList<>(weapons);
    }

    public List<Card> getWeapons() {
        return new ArrayList<>(weapons);
    }

    @Override
    public String getRequest() {
        return Costants.LOADABLE_WEAPONS_MESSAGE;
    }
}
