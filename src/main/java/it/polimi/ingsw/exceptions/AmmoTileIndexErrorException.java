package it.polimi.ingsw.exceptions;

public class AmmoTileIndexErrorException extends Exception {

    public AmmoTileIndexErrorException(String message) {
        super("Index is not valid: "+message);
    }
}
