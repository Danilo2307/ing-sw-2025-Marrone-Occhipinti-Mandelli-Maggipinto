package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.Map;

/**
 * Represents a response event that communicates the flight board's current state to the client.
 *
 * This event contains a mapping of {@link Color} to the corresponding position on the game flight board.
 * The data is encapsulated in a map which is provided to the client through the {@link ViewAPI}.
 *
 * The class is structured to work within the application's event-driven system, implementing the {@link Event}
 * interface and using the Visitor pattern for processing specific event logic.
 *
 * This event can be handled directly by calling the {@code handle(ViewAPI)} method to display
 * the flight board to the client. Additionally, it can be processed through the {@code call(EventVisitor, ViewAPI)}
 * method using a visitor that supports processing {@link FlightBoardResponse} events.
 *
 * Methods:
 * - {@code handle(ViewAPI)} displays the flight board by invoking {@link ViewAPI#showFlightBoard(Map)}.
 * - {@code call(EventVisitor, ViewAPI)} processes the event, delegating to the appropriate visitor method.
 *
 * @param flightMap A map associating {@link Color} to an integer, containing flight board data.
 */
public record FlightBoardResponse(Map<Color, Integer> flightMap) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showFlightBoard(flightMap);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForFlightBoardResponse(this, viewAPI);
    }


}
