package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Events.EventForAbandonedShip;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class AbandonedShip extends Card {
    private final int days;
    private final int cosmicCredits;
    private final int numMembers;
    private int countMember;
    private String isSold;

    public AbandonedShip(int level, int days, int cosmicCredits, int numMembers) {
        super(level);
        this.days = days;
        this.cosmicCredits = cosmicCredits;
        this.numMembers = numMembers;
        this.countMember = 0;
        this.isSold = null;
    }

    public int getDays() {
        return days;
    }

    public int getCosmicCredits() {
        return cosmicCredits;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_ABANDONEDSHIP);
        game.fireEvent(new EventForAbandonedShip(game.getGameStatus(), days, cosmicCredits, numMembers));
    }

    /**
     * Verifies that the game is in the INIT_ABANDONEDSHIP phase before allowing purchase.
     * Ensures that only the player whose turn it is can buy the ship.
     * Throws a CardException if conditions are not met.
     */
    public void buyShip(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_ABANDONEDSHIP) {
            throw new CardException("User '" + username + "' cannot buy the ship in phase: " + game.getGameStatus());
        }
        if (!username.equals(game.getPlayers().get(game.getTurn()))) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        Player p = game.getPlayerFromNickname(username);
        if (p.getTruck().calculateCrew() < numMembers) {
            throw new CardException("User '" + username + "' does not have enough crew");
        }
        isSold = username;
        Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(p), -days);
        p.updateMoney(cosmicCredits);
        endPlay();
    }

    /**
     * Verifies that the game is in the END_ABANDONEDSHIP phase before allowing crew removal.
     * Ensures that only the buyer can remove crew members from housing units.
     * Throws a CardException if conditions are not met.
     */
    public void reduceCrew(String username, int i, int j, int num) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_ABANDONEDSHIP) {
            throw new CardException("User '" + username + "' cannot remove crew in phase: " + game.getGameStatus());
        }
        if (!username.equals(isSold)) {
            throw new CardException("User '" + username + "' is not the buyer");
        }
        if (countMember >= numMembers) {
            throw new CardException("All crew members have already been removed");
        }
        Board board = game.getPlayerFromNickname(username).getTruck();
        if (!board.isValid(i, j) || board.isFree(i, j)) {
            throw new CardException("Invalid coordinates or empty cell: [" + i + "][" + j + "]");
        }
        Component tile = board.getShip()[i][j];
        switch (tile) {
            case HousingUnit cabin -> {
                int idx = board.getHousingUnits().indexOf(cabin);
                if (idx == -1) {
                    throw new CardException("HousingUnit not found in list");
                }
                try {
                    board.getHousingUnits().get(idx).reduceOccupants(num);
                    countMember += num;
                    if (countMember == numMembers) {
                        endPlay();
                    }
                } catch (IllegalArgumentException e) {
                    throw new CardException("Failed to remove " + num + " members: " + e.getMessage());
                }
            }
            default -> throw new CardException("Component at [" + i + "][" + j + "] is not a housing unit");
        }
    }

    /**
     * Sets the game status to END_ABANDONEDSHIP.
     */
    public void endPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.END_ABANDONEDSHIP);
    }

    /**
     * Returns a help string listing available commands depending on current game status.
     * @return help message
     */
    /**
     * Returns a help string listing available commands based on the current game status.
     * Users can call COMPRANAVE during the INIT phase or PASSA at any time.
     * @return help message
     */
    public String help() {
        Game game = Game.getInstance();
        GameStatus status = game.getGameStatus();
        return switch (status) {
            case INIT_ABANDONEDSHIP -> "Available commands: COMPRANAVE, PASSA";
            case END_ABANDONEDSHIP -> "Available commands: REDUCECREW";
            default -> "No commands available in current phase.";
        };
    }
}
