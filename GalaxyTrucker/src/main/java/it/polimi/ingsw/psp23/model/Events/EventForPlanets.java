package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Event for the Planets card:
 * - days lost
 * - number of planets discovered
 * - list of goods on each planet
 */
public class EventForPlanets extends Event {
    private final int daysLost;
    private final List<List<Item>> planetGoods;

    /**
     * Constructs a Planets event.
     *
     * @param newStatus    the updated game status after the event
     * @param daysLost     the number of days lost
     * @param planetGoods  a list of lists, each containing Item goods per planet
     */
    public EventForPlanets(GameStatus newStatus,
                           int daysLost,
                           List<List<Item>> planetGoods) {
        super(newStatus);
        this.daysLost     = daysLost;
        this.planetGoods  = planetGoods;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getNumPlanets() {
        return planetGoods.size();
    }

    public List<List<Item>> getPlanetGoods() {
        return planetGoods;
    }

    /**
     * Converts each list of Item objects into a space-delimited string,
     * returning a list of strings, one per planet.
     */
    private List<String> planetGoodsDescriptions() {
        return planetGoods.stream()
                .map(goodsList -> goodsList.stream()
                        .map(Item::toString)
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.toList());
    }

    /**
     * Builds and returns the UI description for this event in Italian.
     */
    @Override
    public String describe() {
        List<String> descriptions = planetGoodsDescriptions();
        StringBuilder sb = new StringBuilder();
        sb.append("Evento Pianeti:\n")
                .append("  Giorni persi: ").append(daysLost).append("\n")
                .append("  Pianeti scoperti: ").append(getNumPlanets()).append("\n");
        for (int i = 0; i < descriptions.size(); i++) {
            sb.append("    Pianeta ").append(i + 1).append(": ")
                    .append(descriptions.get(i)).append("\n");
        }
        return sb.toString();
    }
}
