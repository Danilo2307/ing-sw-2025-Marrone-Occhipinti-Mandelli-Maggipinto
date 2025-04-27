package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Smugglers adventure card, where players compare
 * cannon strength against the smugglers and win prizes or suffer penalties.
 */
public class Smugglers extends Card {

    /** Required cannon strength to defeat the smugglers. */
    private final int firePower;
    /** Number of items the smugglers steal from a losing player. */
    private final int numItemsStolen;
    /** Number of flight days lost when claiming the reward. */
    private final int days;
    /** List of items awarded to the winning player. */
    private final List<Item> prize;
    /** Nickname of the winning player, if any. */
    private String winner = null;
    /** Nicknames of the losing players. */
    private List<String> losers = new ArrayList<>();

    /**
     * Constructs a Smugglers card with specified level and parameters.
     *
     * @param level the adventure difficulty level
     * @param firePower the smugglers' firepower
     * @param numItemsStolen the number of items stolen on defeat
     * @param days the days lost when claiming the prize
     * @param prize list of items awarded to the victor
     */
    public Smugglers(int level, int firePower, int numItemsStolen, int days, List<Item> prize) {
        super(level);
        this.firePower = firePower;
        this.numItemsStolen = numItemsStolen;
        this.days = days;
        this.prize = prize;
    }

    /** @return the smugglers' firepower threshold */
    public int getFirePower() {
        return firePower;
    }

    /** @return number of items the smugglers will steal on defeat */
    public int getNumItemsStolen() {
        return numItemsStolen;
    }

    /** @return number of flight days lost when claiming the prize */
    public int getDays() {
        return days;
    }

    /**
     * Returns a copy of the prize list for the winner.
     *
     * @return list of prize items
     */
    public List<Item> getPrize() {
        return new ArrayList<>(prize);
    }

    /** @return the nickname of the winning player, or null if none */
    public String getWinner() {
        return winner;
    }

    /**
     * Sets the winning player for this encounter.
     *
     * @param winner nickname of the winner
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /** @return list of nicknames of players who lost this encounter */
    public List<String> getLosers() {
        return losers;
    }

    /**
     * Accepts a visitor for processing this card's logic.
     *
     * @param visitor the visitor handling Smugglers card
     * @return result from the visitor
     */
    public Object call(Visitor visitor) {
        return visitor.visitForSmugglers(this);
    }

    /**
     * Initializes play by firing an event with parameters for this card.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_PLAY);
        game.fireEvent(new Event(
                game.getGameStatus(),
                firePower, numItemsStolen, days, prize
        ));
    }

    /**
     * Executes the encounter: players compare their cannon strength
     * to the smugglers' firepower in order of flight. The first
     * player to exceed the firepower wins; all who fail are recorded
     * as losers. Ties allow the smugglers to advance to the next player.
     *
     * @param inputObject placeholder for user decisions (e.g., battery use)
     */
    public void play(InputObject inputObject) {
        List<Player> players = Game.getInstance().getPlayers();
        for (Player p : players) {
            if (p.getTruck().calculateCannonStrength() > firePower) {
                setWinner(p.getNickname());
                break;
            } else if (p.getTruck().calculateCannonStrength() < firePower) {
                losers.add(p.getNickname());
            }
        }
        endPlay();
    }

    /**
     * Ends the Smugglers encounter and updates the game status to
     * allow the winner to load goods or losers to pick most important goods.
     */
    public void endPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.END_SMUGGLERS);
    }

    // TODO: Handle flight day loss if the winner chooses the prize (update position in loadGoods?)
}
