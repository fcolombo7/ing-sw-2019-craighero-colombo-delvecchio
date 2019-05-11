package it.polimi.ingsw.model.messages.matchanswer;

import it.polimi.ingsw.model.Player;

public abstract class MatchAnswer {
    private Player sender;

    public MatchAnswer(Player sender){
        this.sender=sender;
    }

    public Player getSender() {
        return sender;
    }

    public abstract String getAnswer();
}
