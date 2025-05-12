package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Events.EventForOpenSpace;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.Game.Utility;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OpenSpace extends Card {
    private final Set<Player> resolvers = new HashSet<>();

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
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
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
        game.setCurrentPlayer(game.getPlayers().getFirst());
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
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
        }
        Player p = game.getPlayerFromNickname(username);
        resolvers.add(p);
        List<Player> players = game.getPlayers();
        int strength = p.getTruck().calculateEngineStrength();
        Utility.updatePosition(players, players.indexOf(p), strength);
        if (!resolvers.containsAll(players)) {
            game.getNextPlayer();
        }
        else{
            game.sortPlayersByPosition();
            resolvers.clear();
            game.nextCard();
        }
    }

    /**
     * Returns available commands for the OpenSpace card.
     */
    public String help() {
        if (Game.getInstance().getGameStatus() == GameStatus.INIT_OPENSPACE) {
            return "Available commands: ATTIVA CANNONE, PRONTO";
        }
        return "No commands available in current phase.";
    }

    @Override
    public <T> T call(Visitor<T> visitor) {
        return visitor.visitForOpenSpace(this);
    }

    @Override
    public <T> T call(VisitorCoordinate<T> visitorCoordinate, String username, int i, int j) {
        return visitorCoordinate.visitForOpenSpace(this, username, i, j);
    }

    @Override
    public <T> T call(VisitorUsername<T> visitorUsername, String username) {
        return visitorUsername.visitForOpenSpace(this, username);
    }

    @Override
    public String toString(){
        return
                "È uscita la carta open space\n" +
                "più motori si attivano, più si va avanti!";
    }
}
