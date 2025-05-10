package it.polimi.ingsw.psp23.exceptions;

/**
 * Eccezione lanciata quando non è possibile rimuovere l'equipaggio da una HousingUnit
 * a causa di condizioni non valide (es. numero troppo alto, alieno non rimovibile, ecc.).
 *
 * Usata da Board per fornire un messaggio contestuale al Controller, a partire
 * dalle eccezioni interne sollevate da HousingUnit.
 */
public class CrewOperationException extends GameException {
    /**
     * Costruttore base con solo messaggio.
     * @param message descrizione dell'errore
     */
    public CrewOperationException(String message) {
        super(message);
    }

    /**
     * Costruttore che permette di incapsulare un'eccezione originaria (cause)
     * e di fornire dettagli aggiuntivi nel messaggio.
     * @param message descrizione dell'errore contestuale
     * @param cause eccezione originaria che ha causato l'errore
     */
    public CrewOperationException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }

    @Override
    public String getMessage() {
        String base = super.getMessage();
        Throwable cause = getCause();
        if (cause != null) {
            base += " (causato da "
                    + cause.getClass().getSimpleName()
                    + ": "
                    + cause.getMessage()
                    + ")";
        }
        return base;
    }
}
