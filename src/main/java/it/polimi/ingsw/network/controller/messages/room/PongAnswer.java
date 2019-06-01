package it.polimi.ingsw.network.controller.messages.room;

import it.polimi.ingsw.utils.Constants;

public class PongAnswer extends RoomMessage{
    private static final long serialVersionUID = 6724469736435389115L;

    public PongAnswer(){super(Constants.PONG_ANSWER);}
}
