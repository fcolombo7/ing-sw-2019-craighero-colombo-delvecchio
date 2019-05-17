package it.polimi.ingsw.model.exceptions;

public class PlayerPowerupsException extends RuntimeException {

    public PlayerPowerupsException(String message){super("Player powerups error: " + message);}
}
