package it.polimi.ingsw.psp23.protocol.request;

/** event triggered when the user wants to add the tile currently in hand at ship[x][y] */
public record AddTile(int x, int y) implements Action { }
