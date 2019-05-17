package it.polimi.ingsw.model.exceptions;

public class PlayerWeaponsException extends RuntimeException {

    public PlayerWeaponsException(String message){super("Player weapons error: " + message);}
}
