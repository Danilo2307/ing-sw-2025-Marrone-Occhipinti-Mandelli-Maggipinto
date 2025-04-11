package it.polimi.ingsw.psp23.exceptions;

public class PlayerExistsException extends RuntimeException {
    public PlayerExistsException(String message) {
        super(message);
    }
}
