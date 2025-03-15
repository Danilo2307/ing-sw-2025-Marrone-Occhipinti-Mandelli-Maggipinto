package it.polimi.ingsw.psp23;

import java.util.List;

public abstract class Card {
    private int level;

    Card(int level) {
        this.level = level;
    }

    //we used an abstract method so that every subclass has to implement his own method separately
    public abstract void play(List<Player> players);

    // we are using a getter because we want to know the level of the card later(fro example when we have to create the small decks)
    public int getLevel() {
        return level;
    }

}
