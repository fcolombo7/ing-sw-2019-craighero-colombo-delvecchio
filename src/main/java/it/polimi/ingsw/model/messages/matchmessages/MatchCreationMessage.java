package it.polimi.ingsw.model.messages.matchmessages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messages.SimplePlayer;
import it.polimi.ingsw.utils.Costants;

import java.util.ArrayList;
import java.util.List;

public class MatchCreationMessage extends MatchMessage {
    private static final long serialVersionUID = -2582710102474990566L;
    private int turnNumber;
    private List<SimplePlayer> players;

    public MatchCreationMessage(String recipient, int turnNumber, List<Player> players){
        super(recipient);
        this.turnNumber=turnNumber;
        this.players=new ArrayList<>();
        for (Player p:players) this.players.add(new SimplePlayer(p));
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public List<SimplePlayer> getPlayers() {
        return players;
    }

    @Override
    public String getRequest() {
        return Costants.CREATION_MESSAGE;
    }
}
