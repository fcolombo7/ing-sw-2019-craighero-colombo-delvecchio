package it.polimi.ingsw.network;

import it.polimi.ingsw.utils.Observer;

public interface ClientConnection{

    void closeConnection();

    void asyncAction(String message);

    void run();

    void register(Observer<String> observer);

}