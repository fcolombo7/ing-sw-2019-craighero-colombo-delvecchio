package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class MoveMessage extends MatchMessage {
    private static final long serialVersionUID = -6788939687094472492L;

    private SimplePlayer player;

    public MoveMessage(SimplePlayer player) {
        super(null,Constants.EFFECT_MOVE_MESSAGE);
        this.player=player;
    }

    public SimplePlayer getPlayer() {
        return player;
    }

}
