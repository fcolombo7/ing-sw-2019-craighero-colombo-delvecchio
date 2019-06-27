package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class DiscardWeaponMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = 367762837728389278L;
    private List<Card> weapons;
    public DiscardWeaponMessage(String nickname, List<Card> weapons) {
        super(nickname, Constants.DISCARD_WEAPON_MESSAGE);
        this.weapons=new ArrayList<>(weapons);
    }

    public List<Card> getWeapons() {
        return new ArrayList<>(weapons);
    }
}
