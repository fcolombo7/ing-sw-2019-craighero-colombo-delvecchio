package it.polimi.ingsw.network.controller.messages.matchmessages;

import java.io.Serializable;

public abstract class MatchMessage implements Serializable {
    private static final long serialVersionUID = 2632372168415313474L;

    private String recipient;

    private String request;

    public MatchMessage(String recipient, String request){
        this.recipient=recipient;
        this.request=request;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getRequest(){
        return request;
    }
}