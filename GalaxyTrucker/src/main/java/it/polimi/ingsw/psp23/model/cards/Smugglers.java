package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.exceptions.ContainerException;
import it.polimi.ingsw.psp23.model.Events.EventForSmugglers;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the Smugglers adventure card.
 * Players compare their cannon strength against smugglers to win prizes or
 * suffer penalties. Manages the phases INIT_SMUGGLERS and END_SMUGGLERS.
 */
public class Smugglers extends Card {

    /**
     * Required cannon strength to defeat the smugglers.
     */
    private final int firePower;

    /**
     * Number of items smugglers steal from each losing player.
     */
    private final int numItemsStolen;

    /**
     * Flight days lost when claiming the reward.
     */
    private final int days;

    /**
     * Items awarded to the winning player.
     */
    private final List<Item> prize;

    /**
     * Nickname of the winning player, or null if not determined.
     */
    private String winner = null;

    /**
     * Nicknames of players who failed to defeat smugglers.
     */
    private final List<String> losers = new ArrayList<>();

    /**
     * Tracks how many items have been stolen per player index.
     */
    private List<Integer> lostCount;

    /**
     * Tracks how many prize items have been loaded by the winner.
     */
    private int loadedCount;

    /**
     * Constructs a Smugglers card with the specified parameters.
     *
     * @param level           the difficulty level of this card
     * @param firePower       the cannon strength threshold to win
     * @param numItemsStolen  the number of items stolen on defeat
     * @param days            the days lost penalty when claiming prize
     * @param prize           the list of items awarded to the victor
     */
    public Smugglers(int level,
                     int firePower,
                     int numItemsStolen,
                     int days,
                     List<Item> prize) {
        super(level);
        this.firePower = firePower;
        this.numItemsStolen = numItemsStolen;
        this.days = days;
        this.prize = prize;
        this.loadedCount = 0;


    }

    /**
     * @return the cannon strength needed to defeat smugglers
     */
    public int getFirePower() { return firePower; }

    /**
     * @return the number of items stolen from a losing player
     */
    public int getNumItemsStolen() { return numItemsStolen; }

    /**
     * @return the days lost penalty when claiming the reward
     */
    public int getDays() { return days; }

    /**
     * Returns a defensive copy of the prize list for the winner.
     *
     * @return copy of prize items
     */
    public List<Item> getPrize() { return new ArrayList<>(prize); }

    /**
     * Activates a cannon shot during the INIT_SMUGGLERS phase.
     *
     * @param username the current player's nickname
     * @param i      the row coordinate for the shot
     * @param j      the column coordinate for the shot
     * @throws CardException if phase is invalid or wrong player
     */
    public void activeCannon(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.INIT_SMUGGLERS) {
            throw new CardException("Cannot activate cannon now: phase is " + game.getGameStatus());
        }
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer());
        }
        game.getPlayerFromNickname(username)
                .getTruck()
                .activeCannon(i, j);
    }

    /**
     * Loads prize items onto the winner's ship during the END_SMUGGLERS phase.
     * Applies days penalty only on first load call.
     *
     * @param username the winning player's nickname
     * @param i      the row of target container
     * @param j      the column of target container
     * @throws CardException if phase is invalid, wrong player, or loading fails
     */
    public void loadGoods(String username, int i, int j) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_SMUGGLERS) {
            throw new CardException("Cannot load goods");
        }
        if (!username.equals(winner)) {
            throw new CardException("Player is not winner");
        }
        if (loadedCount == 0) {
            int idx = Game.getInstance().getPlayers().indexOf(username);
            Utility.updatePosition(Game.getInstance().getPlayers(), idx, -days);
        }
        Board board = game.getCurrentPlayer().getTruck();
        Component tile = board.getShip()[i][j];
        if (!(tile instanceof Container container)) {
            throw new CardException("Component at [" + i + "][" + j + "] is not a container");
        }
        int cidx = board.getContainers().indexOf(container);
        if (cidx == -1) {
            throw new CardException("Container not found in list");
        }
        try {
            container.loadItem(prize.get(loadedCount));
            loadedCount++;
            if (loadedCount == prize.size() - 1) {
                winner = null;
                if (allItemsStolen()) {
                    game.nextCard();
                }
            }
        } catch (CardException e) {
            throw new CardException("Item cannot be loaded: " + e.getMessage());
        }
    }

    /**
     * Allows the winner to skip claiming remaining prize items.
     *
     * @param username the winning player's nickname
     * @throws CardException if phase is invalid or wrong player
     */
    public void pass(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_SMUGGLERS) {
            throw new CardException("Winner has not been determined yet");
        }
        if (!username.equals(winner)) {
            throw new CardException("You did not defeat the smugglers: " + username);
        }
        winner = null;
        if (allItemsStolen()) {
            game.nextCard();
        }
    }

    /**
     * Checks if all items have been stolen from losing players.
     *
     * @return true if each player's lostCount >= numItemsStolen
     */
    private boolean allItemsStolen() {
        for (int count : lostCount) {
            if (count < numItemsStolen) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes a precious item from a losing player's container during END_SMUGGLERS.
     *
     * @param username  the losing player's nickname
     * @param i       the row index of the container
     * @param j       the column index of the container
     * @param item the index of the item to remove
     * @throws CardException if phase is invalid, wrong player, or removal fails
     */
    public void removePreciousItem(String username,
                                   int i,
                                   int j,
                                   int item) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_SMUGGLERS) {
            throw new CardException("Cannot remove goods");
        }
        if (!losers.contains(username)) {
            throw new CardException("Player is not loser");
        }
        Board board = game.getPlayerFromNickname(username).getTruck();
        Component tile = board.getShip()[i][j];
        if (!(tile instanceof Container container)) {
            throw new CardException("Component at [" + i + "][" + j + "] is not a container");
        }
        int idx = board.getContainers().indexOf(container);
        if (idx == -1) {
            throw new CardException("Invalid coordinates: no container at [" + i + "][" + j + "]");
        }
        Item target = container.getItems().get(item);
        if (!board.isMostPrecious(target)) {
            throw new CardException("Item " + target.getColor() + " is not the most precious");
        }
        try {
            container.loseItem(target);
            int pidx = game.getPlayers().indexOf(game.getPlayerFromNickname(username));
            lostCount.set(pidx, lostCount.get(pidx) + 1);
            if (allItemsStolen() && winner == null) {
                game.nextCard();
            }
        } catch (ContainerException e) {
            throw new CardException("Failed to remove item: " + e.getMessage());
        }
    }

    /**
     * Accepts a visitor for processing this card.
     *
     * @param visitor the visitor for Smugglers
     * @return visitor's result
     */
    public <T> T call(Visitor<T> visitor) {
        return visitor.visitForSmugglers(this);
    }

    /**
     * Initializes the smugglers encounter phase and fires an event.
     */
    public void initPlay() {
        Game game = Game.getInstance();
        int playerCount = Game.getInstance().getPlayers().size();
        lostCount = new ArrayList<>(Collections.nCopies(playerCount, 0));
        game.setGameStatus(GameStatus.INIT_SMUGGLERS);
        game.fireEvent(new EventForSmugglers(
                game.getGameStatus(),
                firePower,
                numItemsStolen,
                prize,
                days
        ));
    }

    /**
     * Processes the READY command in INIT_SMUGGLERS phase.
     *
     * @param username the player's nickname issuing READY
     * @throws CardException if not INIT_SMUGGLERS
     */
    public void ready(String username) {
        Game game = Game.getInstance();
        if (game.getGameStatus() == GameStatus.INIT_SMUGGLERS) {
            readyStartPhase(username);
        } else {
            throw new CardException("Invalid phase for READY: " + game.getGameStatus());
        }
    }

    /**
     * Core READY logic for INIT_SMUGGLERS.
     *
     * @param username the current player's nickname
     */
    private void readyStartPhase(String username) {
        Game game = Game.getInstance();
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        double power = game.getPlayerFromNickname(username)
                .getTruck()
                .calculateCannonStrength();
        if (power > firePower) {
            winner = username;
            game.setGameStatus(GameStatus.END_SMUGGLERS);
        } else {
            losers.add(username);
        }
        if (game.getCurrentPlayerIndex() >= game.getPlayers().size()) {
            game.setGameStatus(GameStatus.END_SMUGGLERS);
        } else {
            game.getNextPlayer();
        }
    }
    /**
     * Provides usage instructions for the Smugglers card based on current game phase.
     *
     * @return help text listing available commands
     */
    public String help() {
        GameStatus status = Game.getInstance().getGameStatus();
        switch (status) {
            case INIT_SMUGGLERS:
                return "Available commands: ACTIVECANNON, READY";
            case END_SMUGGLERS:
                return "Available commands: LOADGOOD, PASS, REMOVEITEM";
            default:
                return "No commands available in current phase: " + status;
        }
    }

    @Override
    public <T> T call(VisitorUsername<T> visitorUsername, String username) {
        return visitorUsername.visitForSmugglers(this, username);
    }

    @Override
    public <T> T call(VisitorCoordinate<T> visitorCoordinate, String username, int i, int j) {
        return visitorCoordinate.visitForSmugglers(this, username, i, j);
    }

    @Override
    public <T> T call(VisitorCoordinateNum<T> visitorCoordinateNum, String username, int i, int j, int num) {
        return visitorCoordinateNum.visitForSmugglers(this, username, i, j, num);
    }

    @Override
    public String toString(){
        return
                "è uscita la carta Smugglers\n" +
                "la potenza di fuoco è "+ getFirePower() +"\n" +
                "le merci importanti che andrebbero perse sono " + getNumItemsStolen() +"\n" +
                "i giorni persi sono " + getDays() +"\n"+
                "le merci vinte sarebbero: " + getPrize().toString() +"\n";
    }
}
