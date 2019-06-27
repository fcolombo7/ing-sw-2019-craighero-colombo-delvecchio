package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class RecoveringPlayerMessage extends MatchMessage {
    private static final long serialVersionUID = 643696125298644799L;
    private List<SimplePlayer> players;
    private SimpleBoard gameBoard;
    private boolean frenzy;

    public RecoveringPlayerMessage(String nickname, List<Player> players, GameBoard gameBoard, boolean frenzy) {
        super(nickname, Constants.RECOVERING_PLAYER);
        this.players=new ArrayList<>();
        for (Player p:players) this.players.add(new SimplePlayer(p));
        this.gameBoard=gameBoard==null?null:new SimpleBoard(gameBoard);
        this.frenzy=frenzy;
    }
    public RecoveringPlayerMessage(String nickname, List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        super(nickname, Constants.RECOVERING_PLAYER);
        this.players=new ArrayList<>();
        this.players.addAll(players);
        this.gameBoard=gameBoard;
        this.frenzy=frenzy;
    }

    public boolean isFrenzy() {
        return frenzy;
    }

    public List<SimplePlayer> getPlayers() {
        return new ArrayList<>(players);
    }

    public SimpleBoard getGameBoard() {
        return gameBoard;
    }
}
