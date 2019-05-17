package it.polimi.ingsw.model.exceptions;

public class AmmoTileNotValidException extends RuntimeException {
    public AmmoTileNotValidException(String message) {
        super("AmmoTile is not valid: " +message);
    }
}
