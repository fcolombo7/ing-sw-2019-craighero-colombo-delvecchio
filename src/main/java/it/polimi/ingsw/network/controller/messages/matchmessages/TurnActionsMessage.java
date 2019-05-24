package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class TurnActionsMessage extends MatchMessage{
    private static final long serialVersionUID = 530710551631143157L;
    private List<String> actions;

    public TurnActionsMessage(String recipient, List<String> actions) {
        super(recipient,Constants.TURN_AVAILABLE_ACTIONS);
        this.actions =new ArrayList<>(actions);
    }

    public List<String> getActions() {
        return new ArrayList<>(actions);
    }

}
