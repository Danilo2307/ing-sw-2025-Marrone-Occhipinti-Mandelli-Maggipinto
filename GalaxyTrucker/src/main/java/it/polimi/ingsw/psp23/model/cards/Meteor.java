package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.enumeration.Direction;

public class Meteor{
    private final boolean big;
    private final Direction direction;
    private int impactLine;

    public Meteor(boolean big, Direction direction) {
        this.big = big;
        this.direction = direction;
    }

    public boolean isBig(){
        return big;
    }

    public Direction getDirection(){
        return direction;
    }

    public void setImpactLine(int line) { impactLine = line; }

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
