package it.polimi.ingsw.model.messages.matchmessages;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messages.SimpleBoard;
import it.polimi.ingsw.model.messages.SimplePlayer;
import it.polimi.ingsw.utils.Costants;

import java.util.ArrayList;
import java.util.List;

public class MatchUpdateMessage extends MatchMessage {
    private static final long serialVersionUID = 4117044961314593240L;
    private List<SimplePlayer> players;
    private SimpleBoard gameBoard;

    public MatchUpdateMessage(List<Player> players, GameBoard gameBoard) {
        super(null);
        this.players=new ArrayList<>();
        for (Player p:players) this.players.add(new SimplePlayer(p));
        this.gameBoard=new SimpleBoard(gameBoard);
    }

    public List<SimplePlayer> getPlayers() {
        return players;
    }

    public SimpleBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public String getRequest() {
        return Costants.UPDATE_MESSAGE;
    }
}