package it.polimi.ingsw.psp23.model.cards;


import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Events.EventForOpenSpace;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.Utility;

import java.util.List;

public class OpenSpace extends Card{
    // Alberto

    public OpenSpace(int level) {
        super(level);
    }


    public void initPlay(){
        Game.getInstance().setGameStatus(GameStatus.INIT_OPENSPACE);
        Game.getInstance().fireEvent(new EventForOpenSpace(Game.getInstance().getGameStatus()));
    }

    public void play() {
        List<Player> players = Game.getInstance().getPlayers();
        for(Player p : players){
            int potenzaMotrice = p.getTruck().calculateEngineStrength();
            Utility.updatePosition(players, Game.getInstance().getPlayers().indexOf(p) , potenzaMotrice);
        }
        Game.getInstance().setGameStatus(GameStatus.Playing);
    }

}