package it.polimi.ingsw.model.messages.matchmessages;

import java.io.Serializable;

public abstract class MatchMessage implements Serializable {
    private String recipient;

    public MatchMessage(String recipient){
        this.recipient=recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public abstract String getRequest();
}