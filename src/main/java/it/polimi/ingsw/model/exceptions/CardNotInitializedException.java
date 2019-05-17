package it.polimi.ingsw.model.exceptions;

public class CardNotInitializedException extends RuntimeException {
    public CardNotInitializedException(String message){super("Card error: " +message);}
}
