package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the “Stardust” special event card.
 * <p>
 * When played, each player in reverse flight order loses one space
 * (flight day) for every exposed connector on their ship.
 * </p>
 */
public class Stardust extends Card {
    /**
     * Constructs a Stardust card at the specified adventure level.
     *
     * @param level the difficulty level of this card
     */
    public Stardust(int level) {
        super(level);
    }

    /**
     * Prepares the game to resolve this card by setting the status to INIT_PLAY
     * and notifying any observers of the status change.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_PLAY);
        game.fireEvent(new Event(game.getGameStatus()));
    }

    /**
     * Executes the effect of the Stardust card:
     * <ol>
     *   <li>Retrieves the current list of players and inverts it to process from last to first.</li>
     *   <li>For each player in reverse order, calculates how many connectors are exposed on their ship.</li>
     *   <li>Moves that player backward by the number of exposed connectors.</li>
     *   <li>Finally, re-sorts all players by their updated positions.</li>
     * </ol>
     *
     * @param inputObject an unused placeholder for input parameters
     */
    public void play(InputObject inputObject) {
        List<Player> playersInverted = new ArrayList<>(Game.getInstance().getPlayers());
        Collections.reverse(playersInverted);
        for (Player p : playersInverted) {
            int penalty = p.getTruck().calculateExposedConnectors();
            Utility.updatePosition(
                    Game.getInstance().getPlayers(),
                    Game.getInstance().getPlayers().indexOf(p),
                    -penalty
            );
        }
        Game.getInstance().sortPlayersByPosition();
    }
}
