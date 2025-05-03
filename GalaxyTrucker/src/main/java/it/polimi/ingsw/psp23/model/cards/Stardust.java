package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.EventForStardust;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the "Stardust" special event card.
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
     * Prepares the game to resolve this card by setting the status to INIT_STARDUST
     * and notifying any observers of the status change, then immediately executes the effect.
     *
     * @implNote After firing the event, this method directly calls .
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_STARDUST);
        game.fireEvent(new EventForStardust(game.getGameStatus()));
        applyEffect();
    }

    /**
     * Executes the effect of the Stardust card:
     * <ol>
     *   <li>Retrieve the current list of players and invert it to process from last to first.</li>
     *   <li>For each player in reverse order, calculate how many connectors are exposed on their ship.</li>
     *   <li>Move that player backward by the number of exposed connectors.</li>
     *   <li>Set the game status to {@link GameStatus#Playing} to resume normal play.</li>
     * </ol>
     *
     * @implNote This method mutates each player's position based on exposed connectors.
     */
    public void applyEffect() {
        Game game = Game.getInstance();
        List<Player> players = new ArrayList<>(game.getPlayers());
        Collections.reverse(players);
        List<Player> original = game.getPlayers();
        for (Player p : players) {
            int penalty = p.getTruck().calculateExposedConnectors();
            Utility.updatePosition(original, original.indexOf(p), -penalty);
        }
        game.setGameStatus(GameStatus.Playing);
    }
    /**
     * Provides help information for the Stardust card.
     *
     * @return default help text
     */
    public String help() {
        return "No commands available for Stardust; effect is automatic.";
    }
}
