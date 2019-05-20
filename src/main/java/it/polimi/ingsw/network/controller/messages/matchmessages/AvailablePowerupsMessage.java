package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AvailablePowerupsMessage extends MatchMessage {
    private static final long serialVersionUID = -3015921235366893071L;
    private List<Card> powerups;

    public AvailablePowerupsMessage(String recipient, List<Card> powerups) {
        super(recipient);
        this.powerups=new ArrayList<>(powerups);
    }

    public List<Card> getPowerups() {
        return new ArrayList<>(powerups);
    }

    @Override
    public String getRequest() {
        return Constants.AVAILABLE_POWERUPS_MESSAGE;
    }
}
