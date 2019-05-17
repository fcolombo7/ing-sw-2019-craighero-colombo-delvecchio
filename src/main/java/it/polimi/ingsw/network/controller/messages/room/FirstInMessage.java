package it.polimi.ingsw.network.controller.messages.room;

import it.polimi.ingsw.utils.Costants;

public class FirstInMessage extends RoomMessage {
    private static final long serialVersionUID = 5096439031397479438L;
    public FirstInMessage() {
        super();
    }

    @Override
    public String getType() {
        return Costants.FIRST_PLAYER;
    }
}
