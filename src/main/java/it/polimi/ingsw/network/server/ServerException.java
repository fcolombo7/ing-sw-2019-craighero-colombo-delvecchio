package it.polimi.ingsw.network.server;

import java.io.IOException;

public class ServerException extends IOException {
    public ServerException(String msg){
        super(msg);
    }
}
