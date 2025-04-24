package it.polimi.ingsw.psp23.events;


/**
 * Event triggered when a client wants to view their current ship layout.
 * This is a request from the client to the server asking to see its ship.
 * The server will handle this by retrieving the ship data from the model
 * and sending back a response event (e.g. ShipStateResponse) containing the full ship matrix.
 * The client will then use this data to display the ship in the TUI.
 */
public record RequestShip(String username) implements Event {
}
