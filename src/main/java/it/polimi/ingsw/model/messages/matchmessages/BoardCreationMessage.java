package it.polimi.ingsw.model.messages.matchmessages;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.utils.Costants;

public class BoardCreationMessage extends MatchMessage {
    GameBoard gameBoard;

    public BoardCreationMessage(GameBoard gameBoard) {
        super(null);
        this.gameBoard=gameBoard;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public String getRequest() {
        return Costants.BOARD_CREATED_MESSAGE;
    }
}
