package it.polimi.ingsw.network.controller.messages.room;

import it.polimi.ingsw.utils.Constants;

public class DisconnectionMessage extends RoomMessage {
    private static final long serialVersionUID = 8859613378803339712L;

    public DisconnectionMessage(){
        super(Constants.DISCONNECTION);
    }
}
