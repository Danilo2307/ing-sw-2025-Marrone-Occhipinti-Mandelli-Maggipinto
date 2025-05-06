package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

/**
 * Event for the Abandoned Ship card:
 * - days lost
 * - cosmic credits gained
 * - number of crew members gained
 */
public class EventForAbandonedShip extends Event {
    private final int daysLost;
    private final int cosmicCredits;
    private final int numMembers;

    /**
     * Constructs an Abandoned Ship event.
     *
     * @param newStatus      the updated game status after the event
     * @param daysLost       the number of days lost
     * @param cosmicCredits  the amount of cosmic credits gained
     * @param numMembers     the number of crew members gained
     */
    public EventForAbandonedShip(GameStatus newStatus,
                                 int daysLost,
                                 int cosmicCredits,
                                 int numMembers) {
        super(newStatus);
        this.daysLost      = daysLost;
        this.cosmicCredits = cosmicCredits;
        this.numMembers    = numMembers;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getCosmicCredits() {
        return cosmicCredits;
    }

    public int getNumMembers() {
        return numMembers;
    }

    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento Nave abbandonata:\n")
                .append("  Giorni persi: ").append(daysLost).append("\n")
                .append("  Crediti cosmici ottenuti: ").append(cosmicCredits).append("\n")
                .append("  Membri di equipaggio recuperati: ").append(numMembers);
        return sb.toString();
    }
}
