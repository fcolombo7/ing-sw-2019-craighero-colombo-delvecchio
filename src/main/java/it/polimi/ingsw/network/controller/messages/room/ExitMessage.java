package it.polimi.ingsw.network.controller.messages.room;

import it.polimi.ingsw.utils.Costants;

public class ExitMessage extends RoomMessage {
    private static final long serialVersionUID = -4395931511968513882L;
    private String player;
    public ExitMessage(String player){
        this.player=player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public String getType() {
        return Costants.PLAYER_EXIT;
    }
}
