package it.polimi.ingsw.psp23.exceptions;

/**
 * Eccezione lanciata quando si tenta di accedere a una cella della nave:
 * - che non è valida o è vuota (fuori plancia o senza componente), oppure
 * - che contiene un componente di tipo diverso da quello richiesto per l’operazione.
 *
 * Usata nei metodi della classe Board per validare coordinate e tipo prima di eseguire azioni
 * come caricare merci, attivare moduli, ridurre risorse, ecc.
 */

public class IllegalType extends RuntimeException {
    public IllegalType(String message) {
        super(message);
    }

}
