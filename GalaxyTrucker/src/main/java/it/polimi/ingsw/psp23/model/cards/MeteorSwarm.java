package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.List;

public class MeteorSwarm extends Card {
    //Federico
    // lista di meteore già ordinata secondo l'ordine di impatto: from up-down and from left-right
    private final List<Meteor> meteors;
    private boolean isLeader;

    public MeteorSwarm(int level, List<Meteor> meteors) {
        super(level);
        this.meteors = meteors;
        isLeader = true;
    }

    public List<Meteor> getMeteors() {
        return new ArrayList<>(meteors);
    }

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForMeteorSwarm(this);
    }

    public void initPlay() {
        Game.getInstance().setGameStatus(GameStatus.BooleanRequest);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus(), meteors));
    }

    public void play(InputObject inputObject) {
        Player player = Game.getInstance().getCurrentPlayer();
        int impactLine = -1;

        // VANNO MESSI GLI INPUT SU CANNONI E SCUDI

        if(isLeader){
            isLeader = false;
            impactLine = Utility.roll2to12();
        }
        for (Meteor m : meteors) {
            player.getTruck().handleMeteor(m, impactLine, shield);
        }
    }

    public void inputValidity(InputObject inputObject){

    }
}
