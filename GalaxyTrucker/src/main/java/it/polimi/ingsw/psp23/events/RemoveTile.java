package it.polimi.ingsw.psp23.events;

/** event triggered when the user wants to remove the tile at ship[x][y] .
 * ServerEventHandler will call getPlayerFromNickname().getBoard().delete(x,y) */
public record RemoveTile(String username, int x, int y) implements Event { }
