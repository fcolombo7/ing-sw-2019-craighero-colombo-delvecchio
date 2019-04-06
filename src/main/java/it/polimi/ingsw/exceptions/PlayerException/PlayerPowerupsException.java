package it.polimi.ingsw.exceptions.PlayerException;

public class PlayerPowerupsException extends RuntimeException {

    public PlayerPowerupsException(String message){super("Player powerups error: " + message);}
}
