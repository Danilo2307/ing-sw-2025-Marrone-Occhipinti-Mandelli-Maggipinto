package it.polimi.ingsw.psp23.exceptions;

/**
 * Eccezione lanciata quando un componente risulta saldato sulla nave (presente in ship[i][j]),
 * ma non è presente nella rispettiva lista specializzata (es. containers, shields, ecc.).
 *
 * Indica un'incoerenza interna tra la matrice ship e le liste di supporto del model,
 * dovuta a errore di costruzione o rimozione non correttamente tracciata.
 */

public class ComponentMismatchException extends GameException {
    public ComponentMismatchException(String message) {
        super(message);
    }
}
