package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.helpers.Item;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Event for the Smugglers card:
 * - days lost
 * - firepower of smugglers
 * - number of items stolen
 * - prize items
 */
public class EventForSmugglers extends Event {
    private final int daysLost;
    private final int firepower;
    private final int itemsStolen;
    private final List<Item> prizeItems;

    /**
     * Constructs a Smugglers event.
     *
     * @param newStatus    the updated game status after the event
     * @param firepower    the firepower of the smugglers
     * @param itemsStolen  the number of items stolen by smugglers
     * @param prizeItems   the list of Item prizes
     * @param daysLost     the number of days lost
     */
    public EventForSmugglers(GameStatus newStatus,
                             int firepower,
                             int itemsStolen,
                             List<Item> prizeItems,
                             int daysLost) {
        super(newStatus);
        this.firepower   = firepower;
        this.itemsStolen = itemsStolen;
        this.prizeItems  = prizeItems;
        this.daysLost    = daysLost;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getFirepower() {
        return firepower;
    }

    public int getItemsStolen() {
        return itemsStolen;
    }

    public List<Item> getPrizeItems() {
        return prizeItems;
    }

    /**
     * Converts the list of Item objects into a space-delimited string.
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
    public String describe(int gameId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento Contrabbandieri:\n")
                .append("  Giorni persi: ").append(daysLost).append("\n")
                .append("  Potenza di fuoco nemica: ").append(firepower).append("\n")
                .append("  Oggetti rubati: ").append(itemsStolen).append("\n")
                .append("  Oggetti premio: ").append(prizeDescription()).append("\n");
        return sb.toString();
    }
}
