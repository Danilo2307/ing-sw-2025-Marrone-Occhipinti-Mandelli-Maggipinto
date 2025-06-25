package it.polimi.ingsw.psp23.model.helpers;
import it.polimi.ingsw.psp23.model.enumeration.Direction;

/**
 * Represents a single shot fired from a cannon, with properties indicating
 * the size of the shot and the direction in which it is fired.
 *
 * Instances of this class are immutable, meaning that their state
 * cannot be changed after they are created.
 */
public class CannonShot {
    private final boolean big;
    private final Direction direction;

    public CannonShot(boolean big, Direction direction) {
        this.big = big;
        this.direction = direction;
    }

    public boolean isBig() {
        return big;
    }

    public Direction getDirection() {
        return direction;
    }

    public String toString() {
        if(big == true){
            return "Big shot from " + direction;
        }else{
            return "Small shot from " + direction;
        }
    }
}