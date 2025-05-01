package it.polimi.ingsw.psp23.exceptions;

// unchecked exception perchè sono errori del giocatore; il programma non può rimediare a questo, quindi si limita a segnalarlo
public class ComponentStateException extends GameException   {

    public ComponentStateException(String message) {
        super(message);
    }
}
