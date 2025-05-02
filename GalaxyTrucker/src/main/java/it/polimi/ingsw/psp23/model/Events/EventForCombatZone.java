package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Event for the Combat Zone card:
 * - days lost
 * - goods lost
 * - crew members lost
 * - list of challenges
 * - list of cannon shots
 */
public class EventForCombatZone extends Event {
    private final int daysLost;
    private final int goodsLost;
    private final int membersLost;
    private final List<Challenge> challenges;
    private final List<CannonShot> cannonShots;

    /**
     * Constructs a Combat Zone event.
     *
     * @param newStatus    the updated game status after the event
     * @param daysLost     the number of days lost
     * @param goodsLost    the number of goods lost
     * @param membersLost  the number of crew members lost
     * @param challenges   the list of Challenge enums
     * @param cannonShots  the list of CannonShot objects
     */
    public EventForCombatZone(GameStatus newStatus,
                              int daysLost,
                              int goodsLost,
                              int membersLost,
                              List<Challenge> challenges,
                              List<CannonShot> cannonShots) {
        super(newStatus);
        this.daysLost    = daysLost;
        this.goodsLost   = goodsLost;
        this.membersLost = membersLost;
        this.challenges  = challenges;
        this.cannonShots = cannonShots;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getGoodsLost() {
        return goodsLost;
    }

    public int getMembersLost() {
        return membersLost;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public List<CannonShot> getCannonShots() {
        return cannonShots;
    }

    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe() {
        String challDesc = challenges.stream()
                .map(Challenge::toString)
                .collect(Collectors.joining(", "));
        String cannonDesc = cannonShots.stream()
                .map(CannonShot::toString)
                .collect(Collectors.joining(" "));

        StringBuilder sb = new StringBuilder();
        sb.append("Evento Zona di Combattimento:\n")
                .append("  Giorni persi: ").append(daysLost).append("\n")
                .append("  Merci perse: ").append(goodsLost).append("\n")
                .append("  Membri persi: ").append(membersLost).append("\n")
                .append("  Sfide: ").append(challDesc).append("\n")
                .append("  Colpi di cannone: ").append(cannonDesc);
        return sb.toString();
    }
}

