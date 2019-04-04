package it.polimi.ingsw.exceptions;

public class AmmoTileNotValidExceptoin extends Exception {
    public AmmoTileNotValidExceptoin(String message) {
        super("AmmoTile is not valid: " +message);
    }
}
