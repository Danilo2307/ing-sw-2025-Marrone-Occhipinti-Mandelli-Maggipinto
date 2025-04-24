package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class Epidemic extends Card {
    // Alberto

    public Epidemic(int level) {
        super(level);
    }

    public void initPlay(){
        Game.getInstance().setGameStatus(GameStatus.RunningEpidemic);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus()));
    }

    public void play(InputObject input) {

    }

}
