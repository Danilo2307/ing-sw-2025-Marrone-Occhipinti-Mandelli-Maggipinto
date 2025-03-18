package it.polimi.ingsw.psp23.model.cards;

public class CannonShot {
    private final boolean big;
    private final Direction direction;

    CannonShot(boolean big, Direction direction) {
        this.big = big;
        this.direction = direction;
    }

    public boolean isBig() {
        return big;
    }

    public Direction getDirection() {
        return direction;
    }
}