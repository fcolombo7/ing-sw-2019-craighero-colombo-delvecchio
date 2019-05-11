package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.messages.matchanswer.MatchAnswer;
import it.polimi.ingsw.model.messages.matchmessages.MatchMessage;
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