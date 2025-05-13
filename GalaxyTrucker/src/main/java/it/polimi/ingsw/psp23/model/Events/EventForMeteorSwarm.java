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

    /**
     * Constructs a Meteor Swarm event.
     *
     * @param newStatus  the updated game status after the event
     */
    public EventForMeteorSwarm(GameStatus newStatus) {
        super(newStatus);
    }


    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento Sciame di Meteoriti\n");
        return sb.toString();
    }
}
