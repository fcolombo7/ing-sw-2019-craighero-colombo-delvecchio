package it.polimi.ingsw.exceptions;

public class NullMatrixHelperExceptoin extends Exception {
    public NullMatrixHelperExceptoin(String message) {
        super("Matrix not valid." +message);
    }
}
