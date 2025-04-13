package it.polimi.ingsw.psp23.exceptions;

public class DeckAlreadyTakenException extends RuntimeException {
    public DeckAlreadyTakenException(String message) {
        super(message);
    }
}
