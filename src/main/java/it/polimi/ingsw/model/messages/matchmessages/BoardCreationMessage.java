package it.polimi.ingsw.model.messages.matchmessages;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.messages.SimpleBoard;
import it.polimi.ingsw.utils.Costants;

public class BoardCreationMessage extends MatchMessage {
    SimpleBoard gameBoard;

    public BoardCreationMessage(GameBoard gameBoard) {
        super(null);
        this.gameBoard=new SimpleBoard(gameBoard);
    }

    public SimpleBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public String getRequest() {
        return Costants.BOARD_CREATED_MESSAGE;
    }
}
