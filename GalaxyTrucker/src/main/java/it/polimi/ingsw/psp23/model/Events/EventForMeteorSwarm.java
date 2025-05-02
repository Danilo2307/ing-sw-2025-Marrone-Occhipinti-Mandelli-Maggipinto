package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Event for the Meteor Swarm (Open Space / Meteor Swarm) card:
 * - impact line of meteors
 * - list of meteors to resolve
 */
public class EventForMeteorSwarm extends Event {
    private final int impactLine;
    private final List<Meteor> meteors;

    /**
     * Constructs a Meteor Swarm event.
     *
     * @param newStatus  the updated game status after the event
     * @param meteors    the list of Meteor objects drawn
     * @param impactLine the index of the impact line reached
     */
    public EventForMeteorSwarm(GameStatus newStatus,
                               List<Meteor> meteors,
                               int impactLine) {
        super(newStatus);
        this.meteors    = meteors;
        this.impactLine = impactLine;
    }

    public int getImpactLine() {
        return impactLine;
    }

    public List<Meteor> getMeteors() {
        return meteors;
    }

    /**
     * Converts the list of Meteor objects into a space-delimited string,
     * e.g. [Meteor.RED, Meteor.BLUE] → "RED BLUE"
     */
    private String meteorsDescription() {
        return meteors.stream()
                .map(Meteor::toString)
                .collect(Collectors.joining(" "));
    }

    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento Sciame di Meteoriti:\n")
                .append("  Riga di impatto: ").append(impactLine).append("\n")
                .append("  Meteoriti coinvolti: ").append(meteorsDescription());
        return sb.toString();
    }
}
