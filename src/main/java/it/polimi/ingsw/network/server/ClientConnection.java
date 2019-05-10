package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.messages.MatchAnswer;
import it.polimi.ingsw.model.messages.MatchMessage;
import it.polimi.ingsw.network.controller.Room;
import it.polimi.ingsw.utils.Observer;

public interface ClientConnection{

    String getNickname();

    String getMotto();

    void setRoom(Room room);

    Room getRoom();

    boolean isOnline();

    void register(Observer<MatchAnswer> observer);

    void sendMatchMessage(MatchMessage message);
}