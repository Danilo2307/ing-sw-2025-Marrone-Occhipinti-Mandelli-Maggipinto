package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Events.EventForMeteorSwarm;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.*;

/**
 * Represents a Meteor Swarm adventure card.
 * <p>
 * Upon initialization, rolls two dice for each meteor to determine its impact line,
 * stores the result in the meteor, and fires an event for each meteor.
 * During resolution, players call the same command (e.g., ATTIVACANNONE or ATTIVASCUDO),
 * which registers them as ready. Once all have called it, one meteor fires and the set resets.
 * The process repeats until all meteors have impacted.
 * </p>
 */
public class MeteorSwarm extends Card {
    private final List<Meteor> meteors;
    private final Set<String> resolvers = new HashSet<>();
    private int currentIndex;

    public MeteorSwarm(int level, List<Meteor> meteors) {
        super(level);
        this.meteors = new ArrayList<>(meteors);
    }

    public List<Meteor> getMeteors() {
        return new ArrayList<>(meteors);
    }

    @Override
    public Object call(Visitor visitor) {
        return visitor.visitForMeteorSwarm(this);
    }

    /**
     * Rolls dice for each meteor, sets impact lines, fires events, and enters INIT_METEORSWARM.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_METEORSWARM);
        resolvers.clear();
        currentIndex = 0;
        for (Meteor m : meteors) {
            int line = Utility.roll2to12();
            m.setImpactLine(line);
            game.fireEvent(new EventForMeteorSwarm(game.getGameStatus(), List.of(m), line));
        }
    }

    /**
     * Registers the current player as ready, and if all players are ready, fires the next meteor.
     * Must be called in INIT_METEORSWARM phase by each player.
     */
    public void applyEffects(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_METEORSWARM) {
            throw new CardException("Cannot activate meteor swarm now: phase is " + game.getGameStatus());
        }
        resolvers.add(username);
        if (resolvers.size() < game.getPlayers().size()) {
            return; // wait for all players
        }
        // Fire one meteor
        Meteor meteor = meteors.get(currentIndex);
        int line = meteor.getImpactLine();
        for (Player p : game.getPlayers()) {
            p.getTruck().handleMeteor(meteor, line);
        }
        currentIndex++;
        resolvers.clear();
        // Advance phase if done
        if (currentIndex >= meteors.size()) {
            game.setGameStatus(GameStatus.Playing);
        }
    }

    /**
     * Returns available commands for the Meteor Swarm card.
     */
    public String help() {
        if (Game.getInstance().getGameStatus() == GameStatus.INIT_METEORSWARM) {
            return "Available commands: ATTIVACANNONE, ATTIVASCUDO";
        }
        return "No commands available in current phase.";
    }
}
