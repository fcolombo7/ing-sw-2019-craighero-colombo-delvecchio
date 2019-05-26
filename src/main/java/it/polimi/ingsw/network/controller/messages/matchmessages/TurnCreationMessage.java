package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.utils.Constants;

public class TurnCreationMessage extends MatchMessage {
    private static final long serialVersionUID = 3351412056277722075L;
    private String currentPlayer;
    public TurnCreationMessage(String currentPlayer) {
        super(null, Constants.TURN_CREATION_MESSAGE);
        this.currentPlayer=currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
