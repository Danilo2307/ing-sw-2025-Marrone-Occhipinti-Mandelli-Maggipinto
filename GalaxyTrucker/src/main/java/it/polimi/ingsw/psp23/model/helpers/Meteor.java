package it.polimi.ingsw.psp23.model.helpers;

import it.polimi.ingsw.psp23.model.enumeration.Direction;

/**
 * The Meteor class represents a meteor object with specific properties
 * such as its size, direction of movement, and the line of impact it is associated with.
 * Instances of this class are designed to encapsulate the characteristics and
 * behavior of a meteor within the system.
 */
public class Meteor{
    private final boolean big;
    private final Direction direction;
    private int impactLine;

    public Meteor(boolean big, Direction direction) {
        this.big = big;
        this.direction = direction;
    }

    /**
     * Determines if the meteor is categorized as "big" based on its size property.
     *
     * @return true if the meteor is big, false otherwise.
     */
    public boolean isBig(){
        return big;
    }

    /**
     * Retrieves the direction in which the meteor is moving.
     *
     * @return the direction of the meteor as a {@code Direction} enum value.
     */
    public Direction getDirection(){
        return direction;
    }

    /**
     * Sets the impact line for the meteor.
     *
     * @param line the integer value representing the line of impact for the meteor
     */
    public void setImpactLine(int line) { impactLine = line; }

    /**
     * Retrieves the impact line associated with this meteor.
     * The impact line represents the specific line on which the meteor is expected to land.
     *
     * @return an integer representing the impact line of the meteor.
     */
    public int getImpactLine() {return impactLine;}

    @Override
    public String toString() {
        return "Meteor{" +
                "big=" + big +
                ", direction=" + direction +
                ", impactLine=" + impactLine +
                '}';
    }
}
