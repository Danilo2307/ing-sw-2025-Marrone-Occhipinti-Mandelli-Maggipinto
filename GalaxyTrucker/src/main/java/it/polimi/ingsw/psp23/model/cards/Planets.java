package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Events.EventForPlanets;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the Planets card in the game.
 * Manages player landings on planets, cargo loading,
 * and day penalty application.
 */
public class Planets extends Card {
    /** Days lost if a player visits any planet. */
    private final int daysLost;

    /** Goods to load for each planet. */
    private final List<List<Item>> planetGoods;

    /** Nicknames of players who have landed on each planet (null if free). */
    private List<String> planetsOccupied;

    /** Count of goods already loaded per planet. */
    private List<Integer> loadedCount;

    /**
     * Constructs a Planets card.
     * @param level the card level
     * @param daysLost days lost for each landed player
     * @param planetGoods goods lists per planet
     */
    public Planets(int level, int daysLost, List<List<Item>> planetGoods) {
        super(level);
        this.daysLost = daysLost;
        this.planetGoods = planetGoods;
        this.planetsOccupied = new ArrayList<>(Collections.nCopies(planetGoods.size(), null));
        this.loadedCount = new ArrayList<>(Collections.nCopies(planetGoods.size(), 0));
    }

    /** @return days lost penalty. */
    public int getDaysLost() {
        return daysLost;
    }

    /** @return copy of planet goods lists. */
    public List<List<Item>> getPlanetGoods() {
        return new ArrayList<>(planetGoods);
    }

    /** @return array of occupied planet slots. */
    public String[] getPlanetsOccupied() {
        return planetsOccupied.toArray(new String[0]);
    }

    /**
     * Player lands on a planet.
     * @param username player nickname
     * @param i planet index
     * @throws CardException if state invalid or planet occupied
     */
    public void landOnPlanet(String username, int i) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_PLANETS) {
            throw new CardException("Cannot land in " + game.getGameStatus());
        }
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer());
        }
        if (i < 0 || i >= planetsOccupied.size()) {
            throw new CardException("Planet index out of bounds: " + i);
        }
        if (planetsOccupied.get(i) == null) {
            planetsOccupied.set(i, username);
        } else {
            throw new CardException("Planet " + (i + 1) + " is already occupied by " + planetsOccupied.get(i));
        }
        if (game.getCurrentPlayerIndex() < game.getPlayers().size()) {
            game.getNextPlayer();
        } else {
            for (Player p : game.getPlayers().reversed()) {
                if (planetsOccupied.contains(p.getNickname())) {
                    Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(p), -daysLost);
                }
            }
            game.setGameStatus(GameStatus.END_PLANETS);
        }
    }

    /**
     * Player passes landing.
     * @param username player nickname
     * @throws CardException if state invalid
     */
    public void pass(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_PLANETS) {
            throw new CardException("Not in " + GameStatus.INIT_PLANETS);
        }
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer());
        }
        if (game.getCurrentPlayerIndex() < game.getPlayers().size()) {
            game.getNextPlayer();
        } else {
            for (Player p : game.getPlayers().reversed()) {
                if (planetsOccupied.contains(p.getNickname())) {
                    Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(p), -daysLost);
                }
            }
            game.setGameStatus(GameStatus.END_PLANETS);
        }
    }

    /**
     * Loads goods onto the player's truck after landing phase.
     * @param username player nickname
     * @param i row index
     * @param j column index
     * @throws CardException if loading invalid
     */
    public void loadGoods(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_PLANETS) {
            throw new CardException("Cannot load goods");
        }
        if (!planetsOccupied.contains(username)) {
            throw new CardException("Player is not on any planet");
        }
        int player = planetsOccupied.indexOf(username);
        List<Item> items = planetGoods.get(player);
        if (loadedCount.get(player) < items.size()) {
            Board board = game.getCurrentPlayer().getTruck();
            Component[][] ship = board.getShip();
            Component tile = ship[i][j];
            switch (tile) {
                case Container container -> {
                    int index = board.getContainers().indexOf(container);
                    if (index == -1) {
                        throw new CardException("Container not found in 'containers' list");
                    }
                    try {
                        board.getContainers().get(index).loadItem(items.get(loadedCount.get(player)));
                        loadedCount.set(player, loadedCount.get(player) + 1);
                        if (verifyAll()) {
                            game.setGameStatus(GameStatus.Playing);
                        }
                    } catch (CardException c) {
                        throw new CardException("Item cannot be loaded at [" + i + "][" + j + "]: " + c.getMessage());
                    }
                }
                default -> throw new CardException("Component at [" + i + "][" + j + "] is not a container");
            }
        } else {
            throw new CardException("No goods left");
        }
    }

    /**
     * Verifies if all goods for occupied planets are loaded.
     * @return true if all goods loaded, false otherwise
     */
    private boolean verifyAll() {
        for (String player : planetsOccupied) {
            if (player != null && loadedCount.get(planetsOccupied.indexOf(player)) != planetGoods.get(planetsOccupied.indexOf(player)).size()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object call(VisitorParametrico visitorParametrico, int index) {
        if (index < 0 || index >= planetGoods.size()) {
            throw new IllegalArgumentException("Planets index out of bounds in method call");
        }
        return visitorParametrico.visitForPlanets(this, index);
    }

    @Override
    public Object call(Visitor visitor) {
        return visitor.visitForPlanets(this);
    }

    @Override
    public Object call(VisitorUsername visitorUsername, String username) {
        return visitorUsername.visitForPlanets(this, username);
    }

    @Override
    public Object call(VisitorCoordinate visitorCoordinate, String username, int i, int j) {
        return visitorCoordinate.visitForPlanets(this, username, i, j);
    }

    @Override
    public Object call(VisitorUsernameIntero visitorUsernameIntero, String username, int i) {
        return visitorUsernameIntero.visitForPlanets(this, username, i);
    }

    /**
     * Initializes the planet landing phase and fires the event.
     */
    public void initPlay() {
        Game.getInstance().setGameStatus(GameStatus.INIT_PLANETS);
        Game.getInstance().fireEvent(new EventForPlanets(Game.getInstance().getGameStatus(), daysLost, planetGoods));
    }

    /**
     * Provides usage instructions for the Planets card.
     * @return help text detailing available actions
     */
    public String help() {
        Game game = Game.getInstance();
        GameStatus status = game.getGameStatus();
        switch (status) {
            case INIT_PLANETS:
                return "Available commands: LAND, PASS";
            case END_PLANETS:
                return "Available commands: LOADGOODS";
            default:
                return "No commands available in current phase: " + status;
        }
    }
}
