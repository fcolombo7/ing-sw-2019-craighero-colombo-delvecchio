package it.polimi.ingsw.model.messages.matchanswer;

public abstract class MatchAnswer {
    private String sender;

    public MatchAnswer(String sender){
        this.sender=sender;
    }

    public String getSender() {
        return sender;
    }

    public abstract String getAnswer();
}
