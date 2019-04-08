package it.polimi.ingsw.exceptions.playerException;

public class PlayerWeaponsException extends RuntimeException {

    public PlayerWeaponsException(String message){super("Player weapons error: " + message);}
}
