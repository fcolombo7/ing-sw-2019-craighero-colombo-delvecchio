package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class MoveMessage extends MatchMessage {
    private static final long serialVersionUID = -6788939687094472492L;

    private SimplePlayer player;

    public MoveMessage(Player player) {
        super(null,Constants.MOVE_MESSAGE);
        this.player=new SimplePlayer(player);
    }

    public SimplePlayer getPlayer() {
        return player;
    }

}
