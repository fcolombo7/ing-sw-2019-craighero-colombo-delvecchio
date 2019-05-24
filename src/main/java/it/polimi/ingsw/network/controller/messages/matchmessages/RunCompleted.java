package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class RunCompleted extends MatchMessage {
    private static final long serialVersionUID = -8875910121710348173L;

    private SimplePlayer player;

    private int[] newPosition;

    public RunCompleted(SimplePlayer player, int[] newPosition) {
        super(null, Constants.RUN_COMPLETED);
        this.player=player;
        this.newPosition=newPosition;
    }

    public int[] getNewPosition() {
        return newPosition;
    }

    public SimplePlayer getPlayer() {
        return player;
    }

}
