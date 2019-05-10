package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Player;

public abstract class MatchMessage {
    private Player recipient;

    public MatchMessage(Player recipient){
        this.recipient=recipient;
    }

    public Player getRecipient() {
        return recipient;
    }

    public abstract String getRequest();
}