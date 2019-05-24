package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.utils.Constants;

public class BoardUpdateMessage extends MatchMessage {
    private static final long serialVersionUID = -5182145162653926540L;
    private SimpleBoard gameBoard;

    public BoardUpdateMessage(GameBoard gameBoard) {
        super(null, Constants.BOARD_UPDATE_MESSAGE);
        this.gameBoard=new SimpleBoard(gameBoard);
    }

    public SimpleBoard getGameBoard() {
        return gameBoard;
    }
}
