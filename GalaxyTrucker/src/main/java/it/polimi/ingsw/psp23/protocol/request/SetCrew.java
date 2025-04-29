package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.enumeration.Color;

/**
 * Event triggered when the user wants to add crew to a housing unit located at ship[x][y].
 * The crew can be either an alien or a number of astronauts, depending on the `alien` flag.
 * If `alien` is true, the specified alien color is placed at that location.
 * Otherwise, 2 astronauts are added (as defined in the rules).
 * The server will process this by accessing the housing unit at the specified coordinates
 * and modifying its internal state accordingly.
 * Note: the Color enum used here must be serializable (which is true by default in Java).
 */
public record SetCrew(int x, int y, boolean alien, Color color) implements Action {

    public void handle(String username) {

    }
}
