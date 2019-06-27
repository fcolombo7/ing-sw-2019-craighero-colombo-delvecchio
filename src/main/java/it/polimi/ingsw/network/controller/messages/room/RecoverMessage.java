package it.polimi.ingsw.network.controller.messages.room;

import it.polimi.ingsw.utils.Constants;

public class RecoverMessage extends RoomMessage{

    private static final long serialVersionUID = -5770216701554067062L;
    private String player;

    public RecoverMessage(String player){
        super(Constants.PLAYER_RECOVER);
        this.player=player;
    }

    public String getPlayer() {
        return player;
    }
}
