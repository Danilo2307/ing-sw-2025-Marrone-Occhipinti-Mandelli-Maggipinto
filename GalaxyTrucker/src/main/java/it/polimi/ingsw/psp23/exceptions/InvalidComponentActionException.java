package it.polimi.ingsw.psp23.exceptions;

/**
 * Eccezione lanciata quando viene richiesta un'azione non consentita
 * sul tipo specifico di componente presente nella nave.
 * Esempi:
 * - attivare un cannone singolo (anziché doppio),
 * - tentare di attivare un modulo che non supporta l'operazione richiesta.
 * Indica un errore logico nel flusso del gioco, non una violazione strutturale del modello.
 */

public class InvalidComponentActionException extends RuntimeException {
    public InvalidComponentActionException(String message) {
        super(message);
    }
}
