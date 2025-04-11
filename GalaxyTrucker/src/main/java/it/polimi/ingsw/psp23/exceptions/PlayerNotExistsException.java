package it.polimi.ingsw.psp23.exceptions;

public class PlayerNotExistsException extends RuntimeException {
    public PlayerNotExistsException(String message) {
        super(message);
    }
}
