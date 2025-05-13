package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.ItemsEarned;
import it.polimi.ingsw.psp23.model.Events.PlanetOccupation;
import it.polimi.ingsw.psp23.model.Events.TurnOf;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Utility;
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
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
        }
        if (i < 0 || i >= planetsOccupied.size()) {
            throw new CardException("Planet index out of bounds: " + i);
        }
        if (planetsOccupied.get(i-1) == null) {
            game.fireEvent(new PlanetOccupation(game.getGameStatus(), i));
            game.fireEvent(new ItemsEarned(game.getGameStatus()), username);
            planetsOccupied.set(i-1, username);
        } else {
            throw new CardException("Planet " + (i) + " is already occupied by " + planetsOccupied.get(i-1));
        }
        if (game.getCurrentPlayerIndex() < game.getPlayers().size() - 1) {
            game.getNextPlayer();
        } else {
            for (Player p : game.getPlayers().reversed()) {
                if(p != null && planetsOccupied.contains(p.getNickname())) {
                    Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(p), -daysLost);
                }
            }
            game.sortPlayersByPosition();
            game.setGameStatus(GameStatus.END_PLANETS);
        }
    }

    /**
     * Player passes landing or loading item.
     * @param username player nickname
     * @throws CardException if state invalid
     */
    public void pass(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() == GameStatus.END_PLANETS) {
            int playerIndex = planetsOccupied.indexOf(username);
            if (playerIndex >= 0) {
                List<Item> goodsOnPlanet = planetGoods.get(playerIndex);
                int alreadyLoaded = loadedCount.get(playerIndex);
                int totalGoods = goodsOnPlanet.size();
                if (alreadyLoaded < totalGoods) {
                    loadedCount.set(playerIndex, totalGoods);
                    if(verifyAll()){
                        game.nextCard();
                    }
                    return;
                }
            }
        }
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
        }
        if (game.getCurrentPlayerIndex() < game.getPlayers().size() - 1) {
            game.getNextPlayer();
            // game.fireEvent(new TurnOf(game.getGameStatus(), game.getCurrentPlayer().getNickname()));
        } else {
            for (Player p : game.getPlayers().reversed()) {
                if(p != null && planetsOccupied.contains(p.getNickname())) {
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
            throw new CardException("Cannot load goods in this phase");
        }
        if (!planetsOccupied.contains(username)) {
            throw new CardException("Player is not on any planet");
        }
        int player = planetsOccupied.indexOf(username);
        List<Item> items = planetGoods.get(player);
        if (loadedCount.get(player) < items.size()) {
            try {
                Board board = game.getPlayerFromNickname(username).getTruck();
                board.loadGoods(items.get(loadedCount.get(player)), i, j);
                loadedCount.set(player, loadedCount.get(player) + 1);
                if (verifyAll()) {
                    game.nextCard();
                }
            }
            catch (InvalidCoordinatesException | ComponentMismatchException | ContainerException | TypeMismatchException e){
                throw new ItemException("Caricamento non valido", e);
            }
        } else {
            throw new CardException("No goods left");
        }
    }

    /**
     * Verifies if all goods for occupied planets are loaded.
     * @return true if all goods loaded, false otherwise
     */
    // TODO: da rivedere perchè non è detto che i player che atterrano caricano tutte le merci presenti sul pianeta
    private boolean verifyAll() {
        for (String player : planetsOccupied) {
            if (player != null && loadedCount.get(planetsOccupied.indexOf(player)) != planetGoods.get(planetsOccupied.indexOf(player)).size()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <T> T call(VisitorParametrico<T> visitorParametrico, int index) {
        if (index < 0 || index >= planetGoods.size()) {
            throw new CardException("Planets index out of bounds in method call");
        }
        return visitorParametrico.visitForPlanets(this, index);
    }

    @Override
    public <T> T call(Visitor<T> visitor) {
        return visitor.visitForPlanets(this);
    }

    @Override
    public <T> T call(VisitorUsername<T> visitorUsername, String username) {
        return visitorUsername.visitForPlanets(this, username);
    }

    @Override
    public <T> T call(VisitorCoordinate<T> visitorCoordinate, String username, int i, int j) {
        return visitorCoordinate.visitForPlanets(this, username, i, j);
    }

    @Override
    public <T> T call(VisitorUsernameIntero<T> visitorUsernameIntero, String username, int i) {
        return visitorUsernameIntero.visitForPlanets(this, username, i);
    }

    /**
     * Initializes the planet landing phase and fires the event.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_PLANETS);
        game.fireEvent(new EventForPlanets(game.getGameStatus(), daysLost, planetGoods));
        game.setCurrentPlayer(game.getPlayers().getFirst());
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
                return "Available commands: LOADGOODS, PERDI";
            default:
                return "No commands available in current phase: " + status;
        }
    }

    @Override
    public String toString(){

        if(planetGoods.size() == 2){
            return
                    "è uscita la carta Planets\n" +
                            "I pianeti sono 2\n" +
                            "● ->" + planetGoods.get(0).toString() +"\n" +
                            "● ->" + planetGoods.get(1).toString() +"\n" +
                            "i giorni persi sono " + getDaysLost();
        }else if(planetGoods.size() == 3){
            return
                    "è uscita la carta Planets\n" +
                            "I pianeti sono 3\n" +
                            "● ->" + planetGoods.get(0).toString() +"\n" +
                            "● ->" + planetGoods.get(1).toString() +"\n" +
                            "● ->" + planetGoods.get(2).toString() +"\n" +
                            "i giorni persi sono " + getDaysLost();
        }else if(planetGoods.size() == 4){
            return
                    "è uscita la carta Planets\n" +
                            "I pianeti sono 4\n" +
                            "● ->" + planetGoods.get(0).toString() +"\n" +
                            "● ->" + planetGoods.get(1).toString() +"\n" +
                            "● ->" + planetGoods.get(2).toString() +"\n" +
                            "● ->" + planetGoods.get(3).toString() +"\n" +
                            "i giorni persi sono " + getDaysLost();
        }
        return "Errore in carta Planets\n";
    }
}
