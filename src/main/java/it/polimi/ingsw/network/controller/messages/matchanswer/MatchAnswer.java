package it.polimi.ingsw.network.controller.messages.matchanswer;

import java.io.Serializable;

public abstract class MatchAnswer implements Serializable {

    private static final long serialVersionUID = -6093987366223203426L;

    private String sender;

    public MatchAnswer(String sender){
        this.sender=sender;
    }

    public String getSender() {
        return sender;
    }

    public abstract String getAnswer();
}
