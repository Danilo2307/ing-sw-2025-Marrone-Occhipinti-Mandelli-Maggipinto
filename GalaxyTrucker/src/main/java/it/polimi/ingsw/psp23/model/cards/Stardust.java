package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Player;

import java.util.List;

public class Stardust extends Card {
//GIGI
    public Stardust(int level) {
        super(level);
    }

    @Override
    public void play(List<Player> players) {
        for (Player player : players) {
            if (player.isInGame()) {
                int penalty = player.getTruck().getExposedConnectors();
                player.updatePosition(-penalty);
            }
        }
    }
}
