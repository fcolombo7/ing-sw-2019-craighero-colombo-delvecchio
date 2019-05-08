package it.polimi.ingsw.network;

import it.polimi.ingsw.utils.Observer;

public interface ClientConnection{

    String getNickname();

    void setRoom(Room room);

    void asyncAction(String message);

    void register(Observer<String> observer);

}