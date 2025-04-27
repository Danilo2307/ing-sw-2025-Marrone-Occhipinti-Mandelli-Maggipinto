package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Smugglers extends Card {
    // Alberto

    private final int firePower;
    private final int numItemsStolen;
    private final int days;
    private final List<Item> prize;
    private String winner;

    public Smugglers(int level, int firePower, int numItemsStolen, int days, List<Item> prize ) {
        super(level);
        this.firePower = firePower;
        this.numItemsStolen = numItemsStolen;
        this.days = days;
        this.prize = prize;
        winner = null;
    }

    public int getFirePower() {
        return firePower;
    }

    public int getNumItemsStolen() {
        return numItemsStolen;
    }

    public int getDays() {
        return days;
    }

    public List<Item> getPrize() {
        return new ArrayList<Item>(prize);
    }

    public Object call(Visitor visitor){
        return visitor.visitForSmugglers(this);
    }

    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_PLAY);

            game.fireEvent(new Event(
                    game.getGameStatus(),
                    firePower, numItemsStolen, days, prize
            ));
    }

    public void play(InputObject inputObject) {
        List<Player> players = Game.getInstance().getPlayers();
        for(Player p : players){
            if(p.getTruck().calculateCannonStrength() > firePower){ // ovviamente la cannonStrength sarà prima aggiornata dal giocatore che,
                // una volta arrivatagli la carta, decide se usare le batterie o meno(bisogna
                // gestire correttamente tutti i casi limite come ad esempio valori non validi)
                winner = p.getNickname();
                break;
            }
        }
    }

}