package it.polimi.ingsw.psp23.exceptions;

public class CardException extends RuntimeException {
    public CardException() {
        super();
    }
    public CardException(String message) {
        super(message);
    }
    public CardException(String message, Throwable cause) {
        super(message, cause);
    }
}
