package it.polimi.ingsw.psp23.protocol.request;

/** event triggered when the user wants to remove the tile at ship[x][y] .
 * ServerActionHandler will call getPlayerFromNickname().getBoard().delete(x,y) */
public record RemoveTile(int x, int y) implements Action { }
