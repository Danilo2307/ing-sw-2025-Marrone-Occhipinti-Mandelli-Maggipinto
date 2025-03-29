package it.polimi.ingsw.psp23.model.cards;

import java.util.ArrayList;
import java.util.List;

public class Pirates extends Card {
    // Danilo
    private final int prize;
    private final int days;
    private final int firepower;
    private final List <CannonShot> cannonShot;

    public Pirates(int level, int prize, int days, int firepower, List <CannonShot> cannonShot) {
        super(level);
        this.prize = prize;
        this.days = days;
        this.firepower = firepower;
        this.cannonShot = cannonShot;
    }

    public int getPrize() {
        return prize;
    }
    public int getDays() {
        return days;
    }
    public int getFirepower() {
        return firepower;
    }
    public List<CannonShot> getCannonShot() {
        return new ArrayList<>(cannonShot);
    }



}
