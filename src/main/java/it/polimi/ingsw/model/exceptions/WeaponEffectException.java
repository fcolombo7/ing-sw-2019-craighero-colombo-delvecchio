package it.polimi.ingsw.model.exceptions;

public class WeaponEffectException extends RuntimeException {
    public WeaponEffectException(String message){super("Effect not valid: " +message);}
}
