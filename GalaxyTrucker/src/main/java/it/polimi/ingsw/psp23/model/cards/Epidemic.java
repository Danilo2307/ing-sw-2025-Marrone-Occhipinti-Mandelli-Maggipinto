package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Events.EventForEpidemic;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
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
        Game.getInstance().setGameStatus(GameStatus.INIT_EPIDEMIC);
        Game.getInstance().fireEvent(new EventForEpidemic(Game.getInstance().getGameStatus()));
    }

    /**
     * Executes the effect of the Epidemic card: for each pair of connected occupied housing units,
     * removes one occupant from each involved unit.
     */
    public void play() {
        Game.getInstance().setGameStatus(GameStatus.END_EPIDEMIC);
        for (Player p : Game.getInstance().getPlayers()) {
            Board board = p.getTruck();
            List<HousingUnit> housingUnits = board.getHousingUnits();
            List<Boolean> visited = new ArrayList<>();
            int length = housingUnits.size();

            /**
             * Initialize the visited list with false for each housing unit.
             */
            for (int i = 0; i < length; i++) {
                visited.add(false);
            }

            /**
             * Mark units as true in visited if they are occupied and connected to another occupied unit.
             */
            for (int i = 0; i < length; i++) {
                int j = i + 1;
                HousingUnit unitI = housingUnits.get(i);
                if (unitI.getNumAstronaut() > 0 || unitI.getAlien() != null) {
                    while (j < length) {
                        HousingUnit unitJ = housingUnits.get(j);
                        if (unitJ.getNumAstronaut() > 0 || unitJ.getAlien() != null) {
                            int coordXi = unitI.getX();
                            int coordYi = unitI.getY();
                            int coordXj = unitJ.getX();
                            int coordYj = unitJ.getY();
                            if (board.areTilesConnected(coordXi, coordYi, coordXj, coordYj)) {
                                if (!visited.get(i)) {
                                    visited.set(i, true);
                                }
                                if (!visited.get(j)) {
                                    visited.set(j, true);
                                }
                            }
                        }
                        j++;
                    }
                }
            }

            /**
             * Reduce one occupant from each housing unit marked in visited and reset the flag.
             */
            for (int i = 0; i < length; i++) {
                if (visited.get(i)) {
                    housingUnits.get(i).reduceOccupants(1);
                    visited.set(i, false);
                }
            }
        }
        Game.getInstance().setGameStatus(GameStatus.Playing);
    }

    public String help() {
        Game game = Game.getInstance();
        GameStatus status = game.getGameStatus();
        return switch (status) {
            case INIT_EPIDEMIC   -> "Available commands: PLAY";
            default -> "No commands available in current phase.";
        };
    }

}
