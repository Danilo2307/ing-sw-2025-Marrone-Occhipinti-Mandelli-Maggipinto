package it.polimi.ingsw.psp23;

import java.util.List;

public class MeteorSwarm extends Card {
    //Federico
    // lista di meteore già ordinata secondo l'ordine di impatto: from up-down and from left-right
    private final List<Meteor> meteors;

    MeteorSwarm(int level, List<Meteor> meteors) {
        super(level);
        this.meteors = meteors;
    }

    @Override
    public void play(List<Player> players) {

        for (Meteor m : meteors) {
            int impactLine = DiceUtility.roll2to12();
            for (Player p : players) {
                p.getTruck().handleMeteor(m, impactLine);
            }
        }
    }
}
