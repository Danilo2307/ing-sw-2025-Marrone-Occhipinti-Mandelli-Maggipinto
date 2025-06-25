package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.AbstractMap;
import java.util.List;

/**
 * Represents the final ranking in an event-driven system, encapsulating
 * a list of player names and their respective scores. The ranking is provided
 * as a list of key-value pairs, where the key is the player name (String) and
 * the value is the score (Integer).
 *
 * This record implements the Event interface, allowing it to be processed
 * using the Visitor design pattern. It provides mechanisms to handle itself
 * by interacting with a given ViewAPI, or to be processed and consumed by
 * an EventVisitor to perform specific logic or transformations.
 *
 * Methods:
 * - handle(ViewAPI viewAPI): Directly invokes the showRanking method on the
 *   supplied ViewAPI instance to display the ranking to the user.
 * - call(EventVisitor<T> eventVisitor, ViewAPI viewAPI): Accepts an EventVisitor
 *   to process the event using the Visitor pattern, delegating the logic to the
 *   visitor's visitForFinalRanking method.
 */
public record FinalRanking(List<AbstractMap.SimpleEntry<String,Integer>> ranking) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showRanking(ranking);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForFinalRanking(this, viewAPI);
    }
}

