package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Costants;

public class RunRoutineCompleted extends TurnRoutineMessage {
    private static final long serialVersionUID = -8875910121710348173L;

    private SimplePlayer player;

    public RunRoutineCompleted(SimplePlayer player) {
        super(null);
        this.player=player;
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    @Override
    public String getRoutineRequest() {
        return Costants.RUN_ROUTINE_COMPLETED;
    }
}
