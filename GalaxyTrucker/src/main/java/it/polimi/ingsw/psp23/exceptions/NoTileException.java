package it.polimi.ingsw.psp23.exceptions;


/** thrown when a player attempts to draw a tile but no tile is avaible from the source (heap or uncovered)
 * */
public class NoTileException extends RuntimeException {
    public NoTileException(String message) {
        super(message);
    }
}
