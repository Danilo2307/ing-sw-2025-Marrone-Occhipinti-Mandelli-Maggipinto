package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Events.EventForCombatZone;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Card representing a combat encounter where players undergo three sequential challenges
 * (cannon strength, engine strength, and final penalty resolution).
 */
public class CombatZone extends Card {
    private final int daysLost;
    private final int goodsLost;
    private final int membersLost;
    private final List<CannonShot> cannonShot;
    private final List<Challenge> penalties;

    private int countMember;
    private int countGood;
    private String loserSecondChallenge;
    private String loserThirdChallenge;
    private int cannonShotIndex;
    private final Set<String> resolvers;

    /**
     * Constructs a CombatZone with specified difficulty and penalty sequence.
     *
     * @param level       difficulty level of the card
     * @param daysLost    days to deduct from the loser of the first challenge
     * @param goodsLost   goods to remove from the loser of the second challenge
     * @param membersLost crew members to remove in the second challenge
     * @param penalties   ordered list of challenges to apply
     * @param cannonShot  sequence of cannon shots for final resolution
     */
    public CombatZone(int level, int daysLost, int goodsLost, int membersLost,
                      List<Challenge> penalties, List<CannonShot> cannonShot) {
        super(level);
        this.daysLost       = daysLost;
        this.goodsLost      = goodsLost;
        this.membersLost    = membersLost;
        this.penalties      = penalties;
        this.cannonShot     = cannonShot;
        this.countMember    = 0;
        this.countGood      = 0;
        this.loserSecondChallenge = null;
        this.loserThirdChallenge  = null;
        this.cannonShotIndex      = 0;
        this.resolvers            = new HashSet<>();
    }

    /**
     * Returns a copy of the penalty challenges list.
     *
     * @return list of Challenge enums
     */
    public List<Challenge> getPenalties() {
        return new ArrayList<>(penalties);
    }

    /**
     * Returns the number of days to deduct for the first challenge loser.
     *
     * @return daysLost field
     */
    public int getDaysLost() {
        return daysLost;
    }

    /**
     * Returns the number of goods to remove for the second challenge loser.
     *
     * @return goodsLost field
     */
    public int getGoodsLost() {
        return goodsLost;
    }

    /**
     * Returns the number of crew members to remove for the second challenge loser.
     *
     * @return membersLost field
     */
    public int getMembersLost() {
        return membersLost;
    }

    /**
     * Returns a copy of the cannon shot sequence for final resolution.
     *
     * @return list of CannonShot objects
     */
    public List<CannonShot> getCannonShot() {
        return new ArrayList<>(cannonShot);
    }

    /**
     * Finds the player with the smallest crew size among all players.
     *
     * @return Player with minimum crew count
     */
    private Player findMinMembers() {
        List<Player> players = Game.getInstance().getPlayers();
        int minCrew = players.get(0).getTruck().calculateCrew();
        Player minPlayer = players.get(0);
        for (Player p : players) {
            int crew = p.getTruck().calculateCrew();
            if (crew < minCrew) {
                minPlayer = p;
                minCrew = crew;
            }
        }
        return minPlayer;
    }

    /**
     * Finds the player with the lowest cannon strength.
     *
     * @return Player with minimum cannon strength
     */
    private Player findMinCannonStrength() {
        List<Player> players = Game.getInstance().getPlayers();
        double minStrength = players.get(0).getTruck().calculateCannonStrength();
        Player minPlayer = players.get(0);
        for (Player p : players) {
            double strength = p.getTruck().calculateCannonStrength();
            if (strength < minStrength) {
                minPlayer = p;
                minStrength = strength;
            }
        }
        return minPlayer;
    }

    /**
     * Finds the player with the lowest engine strength.
     *
     * @return Player with minimum engine strength
     */
    private Player findMinEngineStrength() {
        List<Player> players = Game.getInstance().getPlayers();
        int minPower = players.get(0).getTruck().calculateEngineStrength();
        Player minPlayer = players.get(0);
        for (Player p : players) {
            int power = p.getTruck().calculateEngineStrength();
            if (power < minPower) {
                minPlayer = p;
                minPower = power;
            }
        }
        return minPlayer;
    }

    /**
     * Activates a cannon tile for the current player when allowed.
     * Applicable in the first or third combat phase if cannon strength is penalty.
     *
     * @param username nickname of the active player
     * @param i        row index of the tile
     * @param j        column index of the tile
     * @throws CardException if not in correct phase or not the player's turn
     */
    public void activeCannon(String username, int i, int j) {
        Game game = Game.getInstance();
        GameStatus status = game.getGameStatus();
        if (!(status == GameStatus.FIRST_COMBATZONE ||
                (status == GameStatus.THIRD_COMBATZONE && penalties.get(2) == Challenge.CannonStrength))) {
            throw new CardException("Not available in the current state");
        }
        if (!username.equals(game.getCurrentPlayer().getNickname())) {
            throw new CardException("Not " + username + "'s turn");
        }
        game.getPlayerFromNickname(username).getTruck().activeCannon(i, j);
    }

    /**
     * Activates a shield tile by the designated player in the third combat phase.
     *
     * @param username nickname of the shield-activating player
     * @param i        row index of the tile
     * @param j        column index of the tile
     * @throws CardException if not in correct phase or wrong player
     */
    public void activeShield(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.THIRD_COMBATZONE) {
            throw new CardException("Not available in the current state");
        }
        if (!username.equals(loserThirdChallenge)) {
            throw new CardException("Not the correct player");
        }
        game.getPlayerFromNickname(username).getTruck().activeShield(i, j);
    }

    /**
     * Activates an engine tile by the losing player in the third combat phase.
     *
     * @param username nickname of the engine-activating player
     * @param i        row index of the tile
     * @param j        column index of the tile
     * @throws CardException if not in correct phase or wrong player
     */
    public void activeEngine(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.THIRD_COMBATZONE) {
            throw new CardException("Not available in the current state");
        }
        if (!username.equals(loserThirdChallenge)) {
            throw new CardException("Not " + username + "'s turn");
        }
        game.getPlayerFromNickname(username).getTruck().activeEngine(i, j);
    }

    /**
     * Applies one cannon shot to the losing player based on random impact line.
     * Advances to Playing status after the final shot.
     */
    private void handleCannonShot() {
        Game game = Game.getInstance();
        CannonShot shot = cannonShot.get(cannonShotIndex);
        int impactLine = Utility.roll2to12();
        game.getPlayerFromNickname(loserThirdChallenge)
                .getTruck().handleCannonShot(shot, impactLine);
        cannonShotIndex++;
        if (cannonShotIndex == cannonShot.size() - 1) {
            game.setGameStatus(GameStatus.Playing);
        }
    }

    /**
     * Removes crew members from the specified housing unit as penalty.
     * Moves to third phase when required removals complete.
     *
     * @param username losing player's nickname
     * @param i        row index of the tile
     * @param j        column index of the tile
     * @param num      number of members to remove
     * @throws CardException if not in correct phase, wrong player, or invalid tile
     */
    public void reduceCrew(String username, int i, int j, int num) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.SECOND_COMBATZONE || membersLost <= 0) {
            throw new CardException("It's not required to remove crew");
        }
        if (!username.equals(loserSecondChallenge)) {
            throw new CardException("User '" + username + "' is not the loser of the second challenge");
        }
        Board board = game.getCurrentPlayer().getTruck();
        Component[][] ship = board.getShip();
        Component tile = ship[i][j];
        switch (tile) {
            case HousingUnit cabin -> {
                int index = board.getHousingUnits().indexOf(cabin);
                if (index == -1) {
                    throw new CardException("HousingUnit not found in 'housingUnit' list");
                }
                try {
                    board.getHousingUnits().get(index).reduceOccupants(num);
                    countMember += num;
                    if (countMember == membersLost) {
                        resolvers.clear();
                        if (penalties.get(2) == Challenge.Members) {
                            loserThirdChallenge = findMinMembers().getNickname();
                        }
                        game.setGameStatus(GameStatus.THIRD_COMBATZONE);
                    }
                } catch (IllegalArgumentException e) {
                    throw new CardException("Failed to remove " + num + " crew members: " + e.getMessage());
                }
            }
            default -> throw new CardException("Component at [" + i + "][" + j + "] is not a housing unit");
        }
    }

    /**
     * Removes the most valuable item from the specified container as penalty.
     * Moves to third phase when required removals complete.
     *
     * @param username losing player's nickname
     * @param i        row index of the tile
     * @param j        column index of the tile
     * @param num     index of the item within the container
     * @throws CardException if not in correct phase, wrong player, or wrong tile/item
     */
    public void removePreciousItem(String username, int i, int j, int num) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.SECOND_COMBATZONE || goodsLost <= 0) {
            throw new CardException("It's not required to remove goods");
        }
        if (!username.equals(loserSecondChallenge)) {
            throw new CardException("User '" + username + "' is not the loser of the second challenge");
        }
        Board board = game.getPlayerFromNickname(username).getTruck();
        Component[][] ship = board.getShip();
        Component tile = ship[i][j];
        switch (tile) {
            case Container container -> {
                int index = board.getContainers().indexOf(tile);
                if (index == -1) {
                    throw new CardException("Invalid coordinates: not a container");
                }
                Item toRemove = container.getItems().get(num);
                if (!board.isMostPrecious(toRemove)) {
                    throw new CardException("Item " + toRemove.getColor() + " is not the most precious");
                }
                try {
                    container.loseItem(toRemove);
                    countGood++;
                    if (countGood == goodsLost) {
                        resolvers.clear();
                        if (penalties.get(2) == Challenge.Members) {
                            loserThirdChallenge = findMinMembers().getNickname();
                        }
                        game.setGameStatus(GameStatus.THIRD_COMBATZONE);
                    }
                } catch (ContainerException e) {
                    throw new CardException("Failed to remove precious item: " + e.getMessage());
                }
            }
            default -> throw new CardException("Component at [" + i + "][" + j + "] is not a container");
        }
    }

    /**
     * Accepts a visitor according to the visitor pattern.
     *
     * @param visitor visitor to handle this card type
     * @return result of the visitor method
     */
    @Override
    public Object call(Visitor visitor) {
        return visitor.visitForCombatZone(this);
    }

    /**
     * Accepts a parameterized visitor with rendering index.
     *
     * @param visitorParametrico visitor to handle this card with index
     * @param index              index parameter (1-3)
     * @return result of the parameterized visitor method
     * @throws IndexOutOfBoundsException if index is out of range
     */
    @Override
    public Object call(VisitorParametrico visitorParametrico, int index) {
        if (index < 1 || index > 3) {
            throw new IndexOutOfBoundsException("Index must be between 1 and 3");
        }
        return visitorParametrico.visitForCombatZone(this, index);
    }

    /**
     * Fires a combat event and sets the initial game phase based on the first penalty.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        game.fireEvent(new EventForCombatZone(
                game.getGameStatus(), daysLost, goodsLost, membersLost, penalties, cannonShot));

        if (penalties.get(0) == Challenge.CannonStrength) {
            game.setGameStatus(GameStatus.FIRST_COMBATZONE);
        } else if (penalties.get(0) == Challenge.Members) {
            Player minPlayer = findMinMembers();
            Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(minPlayer), -daysLost);
            game.sortPlayersByPosition();
            game.setCurrentPlayer(game.getPlayers().getFirst());
        }
    }

    /**
     * Handles the first challenge where players compare cannon strength.
     * Moves to second phase after processing all players.
     *
     * @param username nickname of player signaling completion
     * @throws CardException if not the current player's turn
     */
    private void firstChallenge(String username) {
        Game game = Game.getInstance();
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        resolvers.add(username);
        if (resolvers.size() < game.getPlayers().size()) {
            game.getNextPlayer();
            return;
        }
        Player minPlayer = findMinCannonStrength();
        Utility.updatePosition(game.getPlayers(), game.getPlayers().indexOf(minPlayer), -daysLost);
        game.sortPlayersByPosition();
        game.setCurrentPlayer(game.getPlayers().getFirst());
        resolvers.clear();
        game.setGameStatus(GameStatus.SECOND_COMBATZONE);
    }

    /**
     * Handles the second challenge where players compare engine strength.
     * Determines the loser but does not change phase.
     *
     * @param username nickname of player signaling completion
     * @throws CardException if not the current player's turn
     */
    private void secondChallenge(String username) {
        Game game = Game.getInstance();
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        resolvers.add(username);
        if (resolvers.size() < game.getPlayers().size()) {
            game.getNextPlayer();
            return;
        }
        loserSecondChallenge = findMinEngineStrength().getNickname();
        game.setCurrentPlayer(game.getPlayers().getFirst());
    }

    /**
     * Handles the third challenge or initiates final cannon resolution.
     * Moves to final penalty phase after processing all players.
     *
     * @param username nickname of player signaling completion
     * @throws CardException if not the current player's turn
     */
    private void thirdChallenge(String username) {
        Game game = Game.getInstance();
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        resolvers.add(username);
        if (resolvers.size() < game.getPlayers().size()) {
            game.getNextPlayer();
            return;
        }
        loserThirdChallenge = findMinEngineStrength().getNickname();
        game.setCurrentPlayer(game.getPlayers().getFirst());
        game.setGameStatus(GameStatus.ENDTHIRD_COMBATZONE);
    }

    /**
     * Provides a help string listing available commands based on current phase.
     *
     * @return help text for UI display
     */
    public String help() {
        GameStatus status = Game.getInstance().getGameStatus();
        return switch (status) {
            case FIRST_COMBATZONE   -> "Available commands: ATTIVACANNONE, READY";
            case SECOND_COMBATZONE  -> "Available commands: ATTIVAMOTORE, READY, REMOVEITEM, CREW";
            case THIRD_COMBATZONE   -> "Available commands: ATTIVAMOTORE, READY";
            case ENDTHIRD_COMBATZONE -> "Available commands: ATTIVASCUDO, READY";
            default                 -> "No commands available in current phase.";
        };
    }

    /**
     * Signals readiness in current combat phase and dispatches to appropriate handler.
     *
     * @param username nickname of player signaling readiness
     */
    public void ready(String username) {
        Game game = Game.getInstance();
        GameStatus status = game.getGameStatus();

        if (status == GameStatus.FIRST_COMBATZONE) {
            firstChallenge(username);
        } else if (status == GameStatus.SECOND_COMBATZONE) {
            secondChallenge(username);
        } else if (status == GameStatus.THIRD_COMBATZONE
                && penalties.get(2) == Challenge.CannonStrength) {
            thirdChallenge(username);
        } else if (status == GameStatus.ENDTHIRD_COMBATZONE
                && username.equals(loserThirdChallenge)) {
            handleCannonShot();
        }
    }
}
