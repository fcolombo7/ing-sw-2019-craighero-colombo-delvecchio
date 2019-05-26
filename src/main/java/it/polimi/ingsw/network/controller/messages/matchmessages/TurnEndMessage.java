package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.utils.Constants;

public class TurnEndMessage extends MatchMessage {
    private static final long serialVersionUID = 2615671000693740227L;

    public TurnEndMessage(String receiver) {
        super(receiver, Constants.TURN_END_MESSAGE);
    }
}
