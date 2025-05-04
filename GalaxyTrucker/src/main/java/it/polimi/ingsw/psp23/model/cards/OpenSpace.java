package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Events.EventForOpenSpace;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.Utility;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OpenSpace extends Card {
    private final Set<String> resolvers = new HashSet<>();

    public OpenSpace(int level) {
        super(level);
    }

    /**
     * Activates an engine at the given coordinates during INIT_OPENSPACE phase.
     */
    public void activeEngine(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_OPENSPACE) {
            throw new CardException("Cannot activate engine now: phase is " + game.getGameStatus());
        }
        game.getPlayerFromNickname(username).getTruck().activeEngine(i, j);
    }

    /**
     * Initializes the OpenSpace card by setting up the phase and firing an event.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_OPENSPACE);
        game.fireEvent(new EventForOpenSpace(game.getGameStatus()));
        resolvers.clear();
    }

    /**
     * READY command: registers the player as ready. Once all players are ready,
     * applies the engine strength effect to all and resets the card.
     */
    public void ready(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_OPENSPACE) {
            throw new CardException("Cannot READY now: phase is " + game.getGameStatus());
        }
        if (!game.getPlayers().contains(username)) {
            throw new CardException("Unknown player: " + username);
        }
        resolvers.add(username);
        if (resolvers.size() < game.getPlayers().size()) {
            return; // wait for all players
        }
        // All ready: apply effect
        List<Player> players = game.getPlayers();
        for (Player p : players) {
            int strength = p.getTruck().calculateEngineStrength();
            Utility.updatePosition(players, players.indexOf(p), strength);
        }
        // Reset state
        resolvers.clear();
        game.setGameStatus(GameStatus.Playing);
    }

    /**
     * Returns available commands for the OpenSpace card.
     */
    public String help() {
        if (Game.getInstance().getGameStatus() == GameStatus.INIT_OPENSPACE) {
            return "Available commands: ACTIVEENGINE, READY";
        }
        return "No commands available in current phase.";
    }

    @Override
    public Object call(Visitor visitor) {
        return visitor.visitForOpenSpace(this);
    }

    @Override
    public Object call(VisitorCoordinate visitorCoordinate, String username, int i, int j) {
        return visitorCoordinate.visitForOpenSpace(this, username, i, j);
    }

    @Override
    public Object call(VisitorUsername visitorUsername, String username) {
        return visitorUsername.visitForOpenSpace(this, username);
    }

}
