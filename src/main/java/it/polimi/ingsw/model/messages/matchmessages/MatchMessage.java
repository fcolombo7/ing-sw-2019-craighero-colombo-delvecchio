package it.polimi.ingsw.model.messages.matchmessages;

public abstract class MatchMessage {
    private String recipient;

    public MatchMessage(String recipient){
        this.recipient=recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public abstract String getRequest();
}