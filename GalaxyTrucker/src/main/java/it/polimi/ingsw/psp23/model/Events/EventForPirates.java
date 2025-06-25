package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.helpers.CannonShot;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Event for the Pirates card:
 * - days lost
 * - firepower of pirates
 * - cosmic credits gained as prize
 * - list of cannon shots to resolve
 */
public class EventForPirates extends Event {
    private final int daysLost;
    private final int firepower;
    private final int cosmicCreditsPrize;
    private final List<CannonShot> cannonShots;

    /**
     * Constructs a Pirates event.
     *
     * @param newStatus            the updated game status after the event
     * @param daysLost             the number of days lost
     * @param firepower            the firepower of the pirate ship
     * @param cosmicCreditsPrize   the amount of cosmic credits gained as prize
     * @param cannonShots          the list of CannonShot objects to resolve
     */
    public EventForPirates(GameStatus newStatus,
                           int daysLost,
                           int firepower,
                           int cosmicCreditsPrize,
                           List<CannonShot> cannonShots) {
        super(newStatus);
        this.daysLost            = daysLost;
        this.firepower           = firepower;
        this.cosmicCreditsPrize  = cosmicCreditsPrize;
        this.cannonShots         = cannonShots;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getFirepower() {
        return firepower;
    }

    public int getCosmicCreditsPrize() {
        return cosmicCreditsPrize;
    }

    public List<CannonShot> getCannonShots() {
        return cannonShots;
    }

    /**
     * Converts the list of CannonShot objects into a space-delimited string,
     * e.g. [Shot1, Shot2] → "Shot1 Shot2"
     */
    private String cannonShotsDescription() {
        return cannonShots.stream()
                .map(CannonShot::toString)
                .collect(Collectors.joining(" "));
    }

    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe(int gameId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento Pirati:\n")
                .append("  Giorni persi: ").append(daysLost).append("\n")
                .append("  Potenza di fuoco nemica: ").append(firepower).append("\n")
                .append("  Crediti cosmici in premio: ").append(cosmicCreditsPrize).append("\n")
                .append("  Colpi di cannone da risolvere: ").append(cannonShotsDescription()).append("\n");
        return sb.toString();
    }
}
