package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

/**
 * Event for the Epidemic card:
 * - update only the game status
 */
public class EventForEpidemic extends Event {

    /**
     * Constructs an Epidemic event.
     *
     * @param newStatus the updated game status after the event
     */
    public EventForEpidemic(GameStatus newStatus) {
        super(newStatus);
    }

    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe() {
        // Assuming the UI simply informs of the status change
        return "Evento Epidemia: lo stato di gioco cambia in '"
                + getNewStatus().toString() + "'.\n";
    }
}
