package it.polimi.ingsw.network.controller.messages.room;

import it.polimi.ingsw.utils.Constants;

public class PingMessage extends RoomMessage{

    private static final long serialVersionUID = 1945269029590156557L;

    public PingMessage(){super();}

    @Override
    public String getType() {
        return Constants.PING_CHECK;
    }
}
