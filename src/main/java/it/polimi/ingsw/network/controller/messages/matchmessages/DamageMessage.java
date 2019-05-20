package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class DamageMessage extends MatchMessage {
    private static final long serialVersionUID = 4290816101773311352L;
    private List<SimplePlayer> selected;
    private int value;
    private String player;
    public DamageMessage(String  player, List<Player> selected, int value) {
        super(null);
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

    @Override
    public String getRequest() {
        return Constants.EFFECT_DAMAGE_MESSAGE;
    }
}