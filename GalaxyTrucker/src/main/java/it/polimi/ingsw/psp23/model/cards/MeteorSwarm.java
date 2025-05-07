package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Events.EventForMeteorSwarm;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.*;

/**
 * Represents a Meteor Swarm adventure card.
 * <p>
 * Upon initialization, rolls two dice for each meteor to determine its impact line,
 * fires an event for each, then enters INIT_METEORSWARM.
 * During resolution, players may call ATTIVACANNONE or ATTIVASCUDO in any order,
 * then READY(username) to confirm. Once all have confirmed, one meteor impacts.
 * The cycle repeats until all meteors have impacted.
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
    public <T> T call(Visitor<T> visitor) {
        return visitor.visitForMeteorSwarm(this);
    }

    @Override
    public <T> T call(VisitorCoordinate<T> visitorCoordinate, String username, int i, int j) {
        return visitorCoordinate.visitForMeteorSwarm(this, username, i, j);
    }

    @Override
    public <T> T call(VisitorUsername<T> visitorUsername, String username) {
        return visitorUsername.visitForMeteorSwarm(this, username);
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
     * Activates a cannon at the given coordinates.
     * Can be called by any player during INIT_METEORSWARM.
     */
    public void activeCannon(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_METEORSWARM) {
            throw new CardException("Cannot activate cannon now: phase is " + game.getGameStatus());
        }
        game.getPlayerFromNickname(username).getTruck().activeCannon(i, j);
    }

    /**
     * Activates a shield at the given coordinates.
     * Can be called by any player during INIT_METEORSWARM.
     */
    public void activeShield(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_METEORSWARM) {
            throw new CardException("Cannot activate shield now: phase is " + game.getGameStatus());
        }
        game.getPlayerFromNickname(username).getTruck().activeShield(i, j);
    }

    /**
     * READY command: registers the player as ready. Once all players are ready,
     * one meteor impacts and readiness resets.
     * @param username the player confirming readiness
     */
    public void ready(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_METEORSWARM) {
            throw new CardException("Cannot READY now: phase is " + game.getGameStatus());
        }
        if (!game.getPlayers().contains(username)) {
            throw new CardException("Unknown player: " + username);
        }
        resolvers.add(username);
        if (resolvers.size() < game.getPlayers().size()) {
            return; // wait for all
        }
        // All ready: impact one meteor
        Meteor meteor = meteors.get(currentIndex);
        int line = meteor.getImpactLine();
        for (Player p : game.getPlayers()) {
            p.getTruck().handleMeteor(meteor, line);
        }
        currentIndex++;
        resolvers.clear();
        if (currentIndex >= meteors.size()) {
            game.nextCard();
        }
    }

    /**
     * Returns available commands for the Meteor Swarm card.
     */
    public String help() {
        if (Game.getInstance().getGameStatus() == GameStatus.INIT_METEORSWARM) {
            return "Available commands: ATTIVACANNONE, ATTIVASCUDO, READY";
        }
        return "No commands available in current phase.";
    }

    @Override
    public String toString(){
        return
                "è uscita la carta Meteor Swarm\n" +
                "le meteore sono le seguenti:\n" +
                meteors.toString() +
                "\n";
    }
}
