package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.Events.MeteorIncoming;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.helpers.Meteor;
import it.polimi.ingsw.psp23.model.helpers.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

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
    private final Set<Player> resolvers = new HashSet<>();
    private int currentIndex = 0;

    public MeteorSwarm(int level, List<Meteor> meteors, int id) {
        super(level, id);
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
    public void initPlay(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        game.setGameStatus(GameStatus.INIT_METEORSWARM);
        resolvers.clear();
        for (Meteor m : meteors) {
            int line = Utility.roll2to12();
            m.setImpactLine(line);
        }
        game.setCurrentPlayer(game.getPlayers().getFirst());
        Meteor meteor = meteors.getFirst();
        int line = meteor.getImpactLine();
        game.fireEvent(new MeteorIncoming(game.getGameStatus(), meteor.isBig(), line, meteor.getDirection()));
    }

    /**
     * Activates a cannon at the given coordinates.
     * Can be called by any player during INIT_METEORSWARM.
     */
    public void activeCannon(String username, int i, int j) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.INIT_METEORSWARM) {
            throw new CardException("Cannot activate cannon now: phase is " + game.getGameStatus());
        }
        if(resolvers.contains(game.getPlayerFromNickname(username))){
            throw new CardException("You must wait other players!");
        }
        game.getPlayerFromNickname(username).getTruck().activeCannon(i, j);
    }

    /**
     * Activates a shield at the given coordinates.
     * Can be called by any player during INIT_METEORSWARM.
     */
    public void activeShield(String username, int i, int j) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if (game.getGameStatus() != GameStatus.INIT_METEORSWARM) {
            throw new CardException("Cannot activate shield now: phase is " + game.getGameStatus());
        }
        if(resolvers.contains(game.getPlayerFromNickname(username))){
            throw new CardException("You must wait other players!");
        }
       game.getPlayerFromNickname(username).getTruck().activeShield(i, j);
    }

    /**
     * READY command: registers the player as ready. Once all players are ready,
     * one meteor impacts and readiness resets.
     * @param username the player confirming readiness
     */
    public void ready(String username) {
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Player p = game.getPlayerFromNickname(username);
        if(resolvers.contains(p)){
            throw new CardException("You must wait other players!");
        }
        resolvers.add(p);
        if (!resolvers.containsAll(game.getPlayers())) {
            return; // wait for all
        }
        // All ready: impact one meteor
        Meteor meteor = meteors.get(currentIndex);
        int line = meteor.getImpactLine();
        for (Player player : game.getPlayers()) {
            player.getTruck().handleMeteor(meteor, line);
        }
        currentIndex++;
        resolvers.clear();
        if (currentIndex >= meteors.size()) {
            game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la carta successiva\n")), game.getId());
        }
        else{
            meteor = meteors.get(currentIndex);
            game.fireEvent(new MeteorIncoming(game.getGameStatus(), meteor.isBig(), line, meteor.getDirection()));
        }
    }

    /**
     * Returns available commands for the Meteor Swarm card.
     */
    public String help(String username) {
        return "Available commands: ATTIVACANNONE, ATTIVASCUDO, READY\n";
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
