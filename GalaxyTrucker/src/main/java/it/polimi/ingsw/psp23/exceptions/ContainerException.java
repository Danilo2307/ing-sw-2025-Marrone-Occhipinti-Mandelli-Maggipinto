package it.polimi.ingsw.psp23.exceptions;

/** lanciata quando container pieno/colore item non compatibile e quando container vuoto/item non trovato */
public class ContainerException extends RuntimeException {
    public ContainerException(String message) {
        super(message);
    }
}
