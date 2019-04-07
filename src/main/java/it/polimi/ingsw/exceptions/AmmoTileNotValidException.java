package it.polimi.ingsw.exceptions;

public class AmmoTileNotValidException extends Exception {
    public AmmoTileNotValidException(String message) {
        super("AmmoTile is not valid: " +message);
    }
}
