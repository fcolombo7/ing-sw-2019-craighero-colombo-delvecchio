package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Weapon;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class GrabbedWeaponMessage extends MatchMessage {
    private static final long serialVersionUID = -7866430955703323001L;

    private Card weapon;
    private SimplePlayer player;

    public GrabbedWeaponMessage(SimplePlayer player, Card weapon) {
        super(null, Constants.GRABBED_WEAPON_MESSAGE);
        this.weapon=new Card(weapon);
        this.player=player;
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    public Card getWeapon() {
        return weapon;
    }
}
