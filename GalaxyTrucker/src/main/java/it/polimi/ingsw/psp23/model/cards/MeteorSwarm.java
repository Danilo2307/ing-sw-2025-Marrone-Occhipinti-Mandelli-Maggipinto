package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeteorSwarm extends Card {
    //Federico
    // lista di meteore già ordinata secondo l'ordine di impatto: from up-down and from left-right
    private final List<Meteor> meteors;
    private int impactLine = -1;

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

    public void initPlay() {
        Game.getInstance().setGameStatus(GameStatus.INIT_PLAY);
        for (Meteor m : meteors) {
            impactLine = Utility.roll2to12();
            m.setImpactLine(impactLine);
            Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus(), Collections.singletonList(m), impactLine));
        }
    }

    public void play(InputObject inputObject) {
        List<Player> players = Game.getInstance().getPlayers();
        for (Meteor m : meteors) {
            int impactLine = m.getImpactLine();
            for(Player p : players){
                p.getTruck().handleMeteor(m, impactLine);
            }
        }
    }
}
