package it.polimi.ingsw.network.controller.messages.matchmessages.routinemessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.network.controller.messages.matchmessages.TurnRoutineMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AvailablePowerupsMessage extends TurnRoutineMessage {
    private static final long serialVersionUID = -3015921235366893071L;
    private List<Card> powerups;

    public AvailablePowerupsMessage(String recipient, List<Card> powerups) {
        super(recipient,Constants.AVAILABLE_POWERUPS_MESSAGE);
        this.powerups=new ArrayList<>(powerups);
    }

    public List<Card> getPowerups() {
        return new ArrayList<>(powerups);
    }

}
