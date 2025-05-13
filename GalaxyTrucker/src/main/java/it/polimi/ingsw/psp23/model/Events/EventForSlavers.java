package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

/**
 * Event for the Slavers card:
 * - days lost
 * - slaver ship firepower
 * - number of crew members stolen
 * - cosmic credits gained as prize
 */
public class EventForSlavers extends Event {
    private final int daysLost;
    private final int firepower;
    private final int membersStolen;
    private final int cosmicCreditsPrize;

    /**
     * Constructs a Slavers event.
     *
     * @param newStatus           the updated game status after the event
     * @param daysLost            the number of days lost
     * @param firepower           the firepower of the slaver ship
     * @param membersStolen       the number of crew members stolen by slavers
     * @param cosmicCreditsPrize  the amount of cosmic credits gained as prize
     */
    public EventForSlavers(GameStatus newStatus,
                           int daysLost,
                           int firepower,
                           int membersStolen,
                           int cosmicCreditsPrize) {
        super(newStatus);
        this.daysLost           = daysLost;
        this.firepower          = firepower;
        this.membersStolen      = membersStolen;
        this.cosmicCreditsPrize = cosmicCreditsPrize;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getFirepower() {
        return firepower;
    }

    public int getMembersStolen() {
        return membersStolen;
    }

    public int getCosmicCreditsPrize() {
        return cosmicCreditsPrize;
    }

    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento Schiavisti:\n")
                .append("  Giorni persi: ").append(daysLost).append("\n")
                .append("  Potenza di fuoco: ").append(firepower).append("\n")
                .append("  Membri di equipaggio rubati: ").append(membersStolen).append("\n")
                .append("  Crediti cosmici in premio: ").append(cosmicCreditsPrize).append("\n");
        return sb.toString();
    }
}
