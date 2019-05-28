package it.polimi.ingsw.network.controller.messages.room;

import it.polimi.ingsw.utils.Constants;

public class PongMessage extends RoomMessage{
    private static final long serialVersionUID = 6724469736435389115L;

    public PongMessage(){super(Constants.PONG_ANSWER);}
}
