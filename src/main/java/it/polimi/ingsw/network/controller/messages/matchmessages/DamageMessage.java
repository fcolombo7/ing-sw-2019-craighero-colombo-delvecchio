package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class DamageMessage extends MatchMessage {
    private static final long serialVersionUID = 4290816101773311352L;
    private SimplePlayer selected;
    private int damageValue;
    private int convertedMarks;
    private String player;
    public DamageMessage(String  player, Player selected, int damageValue, int convertedMarks) {
        super(null, Constants.EFFECT_DAMAGE_MESSAGE);
        this.player=player;
        this.selected=new SimplePlayer(selected);
        this.damageValue=damageValue;
        this.convertedMarks=convertedMarks;
    }

    public String getPlayer() {
        return player;
    }

    public int getDamageValue() {
        return damageValue;
    }

    public int getConvertedMarks() {
        return convertedMarks;
    }

    public SimplePlayer getSelected() {
        return selected;
    }
}