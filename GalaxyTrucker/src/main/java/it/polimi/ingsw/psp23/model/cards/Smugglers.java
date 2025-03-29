package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Item;

import java.util.Arrays;

public class Smugglers extends Card {
    // Alberto

    private final int firePower;
    private final int numItemsStolen;
    private final int days;
    private final Item[] prize;

    public Smugglers(int level, int firePower, int numItemsStolen, int days, Item[] prize ) {
        super(level);
        this.firePower = firePower;
        this.numItemsStolen = numItemsStolen;
        this.days = days;
        this.prize = prize;
    }

    public int getFirePower() {
        return firePower;
    }

    public int getNumItemsStolen() {
        return numItemsStolen;
    }

    public int getDays() {
        return days;
    }

    public Item[] getPrize() {
        return Arrays.copyOf(prize, prize.length);
    }

}