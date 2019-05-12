package it.polimi.ingsw.model.messages.matchanswer;

import java.io.Serializable;

public abstract class MatchAnswer implements Serializable {
    private String sender;

    public MatchAnswer(String sender){
        this.sender=sender;
    }

    public String getSender() {
        return sender;
    }

    public abstract String getAnswer();
}
