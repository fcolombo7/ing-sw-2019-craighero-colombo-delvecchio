package it.polimi.ingsw.model.messages.matchmessages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utils.Costants;

import java.util.List;

public class MatchCreationMessage extends MatchMessage {
    private int turnNumber;
    private List<Player> players;

    public MatchCreationMessage(String recipient, int turnNumber, List<Player> players){
        super(recipient);
        this.turnNumber=turnNumber;
        this.players=players;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public String getRequest() {
        return Costants.CREATION_MESSAGE;
    }
}
