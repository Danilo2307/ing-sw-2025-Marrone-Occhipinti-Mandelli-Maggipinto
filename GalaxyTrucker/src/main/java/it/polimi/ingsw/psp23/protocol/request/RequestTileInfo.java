package it.polimi.ingsw.psp23.protocol.request;

/** event triggered when the user wants to view the details of a component at ship[x][y] */
public record RequestTileInfo(int x, int y) implements Action {
}
