package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MarkMessage extends MatchMessage {
    private static final long serialVersionUID = 5517505951328900936L;
    private List<SimplePlayer> selected;
    private int value;
    private String player;
    public MarkMessage(String player, List<Player> selected, int value) {
        super(null, Constants.EFFECT_MARK_MESSAGE);
        this.player=player;
        this.selected=new ArrayList<>(selected.size());
        for(Player p:selected){
            this.selected.add(new SimplePlayer(p));
        }
        this.value=value;
    }

    public String getPlayer() {
        return player;
    }

    public int getValue() {
        return value;
    }

    public List<SimplePlayer> getSelected() {
        return selected;
    }

}
