package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Item;

import java.util.ArrayList;
import java.util.List;

public class AbandonedStation extends Card {

    private final int days;
    private final int numMembers;
    private final List<Item> prize;

    public AbandonedStation(int level, int days, int numMembers, List<Item> prize) {
        super(level);
        this.days = days;
        this.numMembers = numMembers;
        this.prize = prize;
    }

    public int getDays() {
        return days;
    }
    public int getNumMembers() {
        return numMembers;
    }
    public List<Item> getPrize() {
        return new ArrayList<>(prize);
    }

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForAbandonedStation(this);
    }
}