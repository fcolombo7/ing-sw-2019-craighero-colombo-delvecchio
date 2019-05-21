package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class RunRoutineCompleted extends TurnRoutineMessage {
    private static final long serialVersionUID = -8875910121710348173L;

    private SimplePlayer player;

    private int[] newPosition;

    public RunRoutineCompleted(SimplePlayer player,int[] newPosition) {
        super(null);
        this.player=player;
        this.newPosition=newPosition;
    }

    public int[] getNewPosition() {
        return newPosition;
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    @Override
    public String getRoutineRequest() {
        return Constants.RUN_ROUTINE_COMPLETED;
    }
}
