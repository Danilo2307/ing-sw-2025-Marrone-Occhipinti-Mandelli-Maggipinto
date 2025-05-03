package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Events.EventForMeteorSwarm;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.*;

/**
 * Represents a Meteor Swarm adventure card.
 * <p>
 * Upon initialization, rolls two dice for each meteor to determine its impact line,
 * stores the result in the meteor, and fires an event for each meteor.
 * During play, applies each meteor's impact to every player's truck.
 * </p>
 */
public class MeteorSwarm extends Card {
    /**
     * List of meteors in the order they will impact (top-to-bottom, left-to-right).
     */
    private final List<Meteor> meteors;
    private final Set<String> resolvers = new HashSet<>();

    /**
     * Constructs a MeteorSwarm card with the specified difficulty level and meteors.
     *
     * @param level   the difficulty level of this card
     * @param meteors the list of meteors, ordered by impact sequence
     */
    public MeteorSwarm(int level, List<Meteor> meteors) {
        super(level);
        this.meteors = meteors;
    }

    /**
     * Returns a defensive copy of the meteor list.
     *
     * @return a new list containing all meteors
     */
    public List<Meteor> getMeteors() {
        return new ArrayList<>(meteors);
    }

    @Override
    public Object call(Visitor visitor) {
        return visitor.visitForMeteorSwarm(this);
    }

    /**
     * Initializes the play phase for the Meteor Swarm.
     * Rolls two dice for each meteor to set its impact line,
     * stores the result in the meteor, and fires an event to notify listeners.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_METEORSWARM);

        for (Meteor meteor : meteors) {
            int impactLine = Utility.roll2to12();
            meteor.setImpactLine(impactLine);
            game.fireEvent(new EventForMeteorSwarm(
                    game.getGameStatus(),
                    Collections.singletonList(meteor),
                    impactLine
            ));
        }
    }

    /**
     * Executes the Meteor Swarm effect.
     * Applies each stored meteor impact line to every player's truck.
     *
     */
    public void applyEffect(String username) {
        resolvers.add(username);
        List<Player> players = Game.getInstance().getPlayers();
        if(resolvers.size() == players.size()){
            for (Meteor meteor : meteors) {
                int impactLine = meteor.getImpactLine();
                for (Player player : players) {
                    player.getTruck().handleMeteor(meteor, impactLine);
                }
            }
        }
        Game.getInstance().setGameStatus(GameStatus.Playing);
    }

}
