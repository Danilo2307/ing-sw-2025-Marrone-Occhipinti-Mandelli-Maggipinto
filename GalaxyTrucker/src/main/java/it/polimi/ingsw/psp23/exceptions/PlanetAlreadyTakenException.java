package it.polimi.ingsw.psp23.exceptions;

public class PlanetAlreadyTakenException extends RuntimeException {
    public PlanetAlreadyTakenException(String message) {
        super(message);
    }
}
