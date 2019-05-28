package it.polimi.ingsw.network.controller.messages.room;

import it.polimi.ingsw.utils.Constants;

public class JoinMessage extends RoomMessage{

    private static final long serialVersionUID = -9098788882872864987L;

    private String player;

    public JoinMessage(String player){
        super(Constants.PLAYER_JOIN);
        this.player=player;
    }

    public String getPlayer() {
        return player;
    }
}
