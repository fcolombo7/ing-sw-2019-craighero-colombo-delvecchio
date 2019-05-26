package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class MarkMessage extends MatchMessage {
    private static final long serialVersionUID = 5517505951328900936L;
    private SimplePlayer selected;
    private int value;
    private String player;
    public MarkMessage(String player, Player selected, int value) {
        super(null, Constants.EFFECT_MARK_MESSAGE);
        this.player=player;
        this.selected=new SimplePlayer(selected);
        this.value=value;
    }

    public String getPlayer() {
        return player;
    }

    public int getValue() {
        return value;
    }

    public SimplePlayer getSelected() {
        return selected;
    }

}
