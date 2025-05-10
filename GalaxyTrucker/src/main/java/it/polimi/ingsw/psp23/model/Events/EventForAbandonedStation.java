package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Event for the Abandoned Station card:
 * - days lost
 * - number of crew members gained
 * - prize items
 */
public class EventForAbandonedStation extends Event {
    private final int daysLost;
    private final int numMembers;
    private final List<Item> prizeItems;

    /**
     * Constructs an Abandoned Station event.
     *
     * @param newStatus    the updated game status after the event
     * @param daysLost     the number of days lost
     * @param numMembers   the number of crew members gained
     * @param prizeItems   the list of Item prizes
     */
    public EventForAbandonedStation(GameStatus newStatus,
                                    int daysLost,
                                    int numMembers,
                                    List<Item> prizeItems) {
        super(newStatus);
        this.daysLost   = daysLost;
        this.numMembers = numMembers;
        this.prizeItems = prizeItems;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public List<Item> getPrizeItems() {
        return prizeItems;
    }

    /**
     * Converts the list of Item objects into a single space-delimited string,
     * e.g. ["Blue", "Yellow"] → "Blue Yellow"
     */
    private String prizeDescription() {
        return prizeItems.stream()
                .map(Item::toString)
                .collect(Collectors.joining(" "));
    }

    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento Stazione abbandonata:\n")
                .append("  Giorni persi: ").append(daysLost).append("\n")
                .append("  Membri di equipaggio recuperati: ").append(numMembers).append("\n")
                .append("  Oggetti premio: ").append(prizeDescription()).append("\nSi parte dal leader\n");
        return sb.toString();
    }
}
