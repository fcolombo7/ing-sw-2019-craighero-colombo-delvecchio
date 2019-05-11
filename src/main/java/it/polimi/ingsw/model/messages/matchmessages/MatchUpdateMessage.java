package it.polimi.ingsw.model.messages.matchmessages;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utils.Costants;

import java.util.List;

public class MatchUpdateMessage extends MatchMessage {
    List<Player> players;
    GameBoard gameBoard;

    public MatchUpdateMessage(List<Player> players, GameBoard gameBoard) {
        super(null);
        this.players=players;
        this.gameBoard=gameBoard;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public String getRequest() {
        return Costants.UPDATE_MESSAGE;
    }
}
