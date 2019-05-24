package it.polimi.ingsw.network.controller.messages.matchanswer;

import java.io.Serializable;

public abstract class MatchAnswer implements Serializable {

    private static final long serialVersionUID = -6093987366223203426L;

    private String sender;
    private String answer;

    public MatchAnswer(String sender, String answer){
        this.sender=sender;
        this.answer=answer;
    }

    public String getSender() {
        return sender;
    }

    public String getAnswer(){
        return answer;
    }
}
