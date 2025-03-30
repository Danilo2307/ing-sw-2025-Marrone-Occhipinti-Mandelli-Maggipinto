package it.polimi.ingsw.psp23.model.cards;

import java.util.ArrayList;
import java.util.List;

public class MeteorSwarm extends Card {
    //Federico
    // lista di meteore già ordinata secondo l'ordine di impatto: from up-down and from left-right
    private final List<Meteor> meteors;

    public MeteorSwarm(int level, List<Meteor> meteors) {
        super(level);
        this.meteors = meteors;
    }

    public List<Meteor> getMeteors() {
        return new ArrayList<>(meteors);
    }

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForMeteorSwarm(this);
    }
}
