package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * Represents a response containing information about a ship
 * and its associated owner. It is designed to be communicated
 * through the system and processed by event handling mechanisms.
 *
 * The ShipResponse includes a ship represented as a 2D array of
 * components and the owner's name. This response can be displayed
 * via a ViewAPI or processed by an EventVisitor using the visitor
 * pattern.
 *
 * This class implements the Event interface, enabling it to be used
 * in the event system.
 *
 * Fields:
 * - ship: A 2D array of Component objects representing the ship's structure.
 * - owner: A string indicating the owner's name of the ship.
 *
 * Methods:
 * - handle(ViewAPI viewAPI): Displays the ship and its owner using the provided
 *   ViewAPI instance.
 * - call(EventVisitor<T> eventVisitor, ViewAPI viewAPI): Allows the event
 *   visitor to process this specific event by invoking the appropriate
 *   visit method for ShipResponse.
 */
public record ShipResponse(Component[][] ship, String owner) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showShip(ship, owner);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForShipResponse(this, viewAPI);
    }

}