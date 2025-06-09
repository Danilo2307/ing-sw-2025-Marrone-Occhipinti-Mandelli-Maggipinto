package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

/**
 * Event for the Stardust card:
 * - update only the game status
 */
public class EventForStardust extends Event {

    /**
     * Constructs a Stardust event.
     *
     * @param newStatus the updated game status after the event
     */
    public EventForStardust(GameStatus newStatus) {
        super(newStatus);
    }

    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe(int gameId) {
        // Assuming the UI simply informs of the status change
        return "Evento Polvere di stelle: lo stato di gioco cambia in '"
                + getNewStatus().toString() + "'.\n";
    }
}
