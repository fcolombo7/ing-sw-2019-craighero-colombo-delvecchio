package it.polimi.ingsw.network.controller.messages.room;

import java.io.Serializable;

public abstract class RoomMessage implements Serializable {
    private static final long serialVersionUID = -4898855828474840649L;
    private String type;
    RoomMessage(String type){
        this.type=type;
    }
    public String getType(){
        return type;
    }
}