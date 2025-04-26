package it.polimi.ingsw.psp23.model.cards;


import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.Utility;

public class OpenSpace extends Card{
    // Alberto

    public OpenSpace(int level) {
        super(level);
    }


    public void initPlay(){
        Game.getInstance().setGameStatus(GameStatus.RunningOpenSpace);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus(), meteors, impactLine));
    }
//TODO: si presume il player abbia già attivato i motori che voleva attivare e che abbia già rimosso le batterie da dove voleva rimuoverle

    public void play(InputObject inputObject) {
        Player player = Game.getInstance().getCurrentPlayer();
        int potenzaMotrice = player.getTruck().calculateEngineStrength();
        Utility.updatePosition(Game.getInstance().getPlayers(), Game.getInstance().getPlayers().indexOf(player) , potenzaMotrice);
    }

}