package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

/**
 * Event for the OpenSpace card:
 * - update only the game status
 */
public class EventForOpenSpace extends Event {

    /**
     * Constructs an OpenSpace event.
     *
     * @param newStatus the updated game status after the event
     */
    public EventForOpenSpace(GameStatus newStatus) {
        super(newStatus);
    }

    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe(int gameId) {
        // Assuming the UI simply informs of the status change
        return "Evento Spazio Aperto: lo stato di gioco cambia in '"
                + getNewStatus().toString() + "'.\n";
    }
}
