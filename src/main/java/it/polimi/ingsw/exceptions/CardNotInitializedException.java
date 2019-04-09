package it.polimi.ingsw.exceptions;

public class CardNotInitializedException extends RuntimeException {
    public CardNotInitializedException(String message){super("Card error: " +message);}
}
