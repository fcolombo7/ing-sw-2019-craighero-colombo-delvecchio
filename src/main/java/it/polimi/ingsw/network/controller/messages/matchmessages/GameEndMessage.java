package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GameEndMessage extends MatchMessage {
    private static final long serialVersionUID = 1598913597436929488L;
    private List<SimplePlayer> players;
    public GameEndMessage(List<Player> players) {
        super(null, Constants.GAME_END_MESSAGE);
        this.players=new ArrayList<>(players.size());
        for(Player p:players){
            this.players.add(new SimplePlayer(p));
        }
    }
    public GameEndMessage(){
        super(null, Constants.GAME_END_MESSAGE);
        this.players=new ArrayList<>();
    }

    public void setPlayers(List<SimplePlayer> players) {
        this.players = players;
    }

    public List<SimplePlayer> getPlayers() {
        return players;
    }
}
