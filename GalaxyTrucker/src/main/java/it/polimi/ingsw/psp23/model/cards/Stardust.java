package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;

import java.util.List;

public class Stardust extends Card {
    //GIGI
    public Stardust(int level) {
        super(level);
    }

    @Override
    public void play(List<Player> players) {
        int size = players.size();
        for (size = size-1 ;size >= 0; size--) {
            if (players.get(size).isInGame()) {
                int penalty = players.get(size).getTruck().calculateExposedConnectors();
                Utility.updatePosition(players,size,-penalty);
            }
        }
    }
}