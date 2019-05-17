package it.polimi.ingsw.network.controller.messages.room;

import java.io.Serializable;

public abstract class RoomMessage implements Serializable {
    private static final long serialVersionUID = -4898855828474840649L;
    public abstract String getType();
}
