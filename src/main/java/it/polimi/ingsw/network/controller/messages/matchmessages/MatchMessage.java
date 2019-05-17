package it.polimi.ingsw.network.controller.messages.matchmessages;

import java.io.Serializable;

public abstract class MatchMessage implements Serializable {
    private static final long serialVersionUID = 2632372168415313474L;
    private String recipient;

    public MatchMessage(String recipient){
        this.recipient=recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public abstract String getRequest();
}