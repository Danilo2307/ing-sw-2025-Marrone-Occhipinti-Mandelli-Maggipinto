package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Events.EventForEpidemic;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.List;

/**
 * Represents an Epidemic card that removes one occupant from each pair of connected occupied housing units.
 */
public class Epidemic extends Card {

    /**
     * Constructs an Epidemic card of the specified level.
     *
     * @param level the difficulty level of the card
     */
    public Epidemic(int level) {
        super(level);
    }

    /**
     * Initializes the play of the Epidemic card by setting the game status and firing the corresponding event.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_EPIDEMIC);
        game.fireEvent(new EventForEpidemic(game.getGameStatus()));
    }

    /**
     * Executes the effect of the Epidemic card. Must be called in INIT_EPIDEMIC phase.
     * For each pair of connected occupied housing units, removes one occupant from each involved unit.
     * Finally resets the game status to Playing.
     *
     * @throws CardException if called outside INIT_EPIDEMIC or removal fails
     */
    public void play() {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_EPIDEMIC) {
            throw new CardException("Cannot apply epidemic now: phase is " + game.getGameStatus());
        }
        for (Player p : game.getPlayers()) {
            Board board = p.getTruck();
            List<HousingUnit> housingUnits = board.getHousingUnits();
            int length = housingUnits.size();
            boolean[] connected = new boolean[length];
            // Determine connected pairs
            for (int i = 0; i < length; i++) {
                HousingUnit ui = housingUnits.get(i);
                if (ui.getNumAstronaut() > 0 || ui.getAlien() != null) {
                    for (int j = i + 1; j < length; j++) {
                        HousingUnit uj = housingUnits.get(j);
                        if ((uj.getNumAstronaut() > 0 || uj.getAlien() != null)
                                && board.areTilesConnected(ui.getX(), ui.getY(), uj.getX(), uj.getY())) {
                            connected[i] = true;
                            connected[j] = true;
                        }
                    }
                }
            }
            // Remove occupants
            for (int i = 0; i < length; i++) {
                if (connected[i]) {
                    try {
                        housingUnits.get(i).reduceOccupants(1);
                    } catch (IllegalArgumentException e) {
                        throw new CardException("Epidemic removal failed on housing unit at index " + i + ": " + e.getMessage());
                    }
                }
            }
        }
        // finalize
        Game.getInstance().setGameStatus(GameStatus.Playing);
    }

    /**
     * Provides help text for the Epidemic card. No player commands are available.
     *
     * @return help message
     */
    public String help() {
        return "This card triggers immediately: no commands available.";
    }

    @Override
    public Object call(Visitor visitor) {
        return visitor.visitForEpidemic(this);
    }
}
