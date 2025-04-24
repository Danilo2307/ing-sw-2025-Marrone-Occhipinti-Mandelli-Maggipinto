package it.polimi.ingsw.psp23.events;

/** event triggered when the user wants to view the details of a component at ship[x][y] */
public record RequestTileInfo(String username, int x, int y) implements Event {
}
