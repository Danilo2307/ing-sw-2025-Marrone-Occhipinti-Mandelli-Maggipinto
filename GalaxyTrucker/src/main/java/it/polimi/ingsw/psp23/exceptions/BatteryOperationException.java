package it.polimi.ingsw.psp23.exceptions;

/**
 * Eccezione lanciata quando si verifica un errore durante un'operazione
 * relativa a un modulo BatteryHub, come la rimozione di un numero non valido di batterie.
 *
 * Indica che il numero di batterie da rimuovere richiesto supera la quantità disponibile o il valore passato è negativo.
 *
 * Viene usata per arricchire il contesto dell’errore nei metodi della classe Board,
 * permettendo al Controller di gestire in modo mirato i problemi legati alla gestione delle batterie.
 */

public class BatteryOperationException extends RuntimeException {
    public BatteryOperationException(String message) {
        super(message);
    }
}
