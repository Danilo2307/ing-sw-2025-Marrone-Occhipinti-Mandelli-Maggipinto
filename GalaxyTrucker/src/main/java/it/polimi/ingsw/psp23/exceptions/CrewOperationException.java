package it.polimi.ingsw.psp23.exceptions;

/**
 * Eccezione lanciata quando non è possibile rimuovere l'equipaggio da una HousingUnit
 * a causa di condizioni non valide (es. numero troppo alto, alieno non rimuovibile, ecc.).
 *
 * Usata da Board per fornire un messaggio contestuale al Controller, a partire
 * dalle eccezioni interne sollevate da HousingUnit.
 */

public class CrewOperationException extends RuntimeException {
    public CrewOperationException(String message) {
        super(message);
    }
}
