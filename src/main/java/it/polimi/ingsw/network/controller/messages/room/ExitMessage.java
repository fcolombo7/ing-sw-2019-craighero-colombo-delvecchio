package it.polimi.ingsw.network.controller.messages.room;

import it.polimi.ingsw.utils.Constants;

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
        return Constants.PLAYER_EXIT;
    }
}
