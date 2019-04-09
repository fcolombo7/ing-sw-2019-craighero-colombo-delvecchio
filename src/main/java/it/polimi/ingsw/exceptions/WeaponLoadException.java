package it.polimi.ingsw.exceptions;

public class WeaponLoadException extends RuntimeException {
    public WeaponLoadException(String message){super("Loaded value not valid: " +message);}
}
