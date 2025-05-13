package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.CosmicCreditsEarned;
import it.polimi.ingsw.psp23.model.Events.EnemyDefeated;
import it.polimi.ingsw.psp23.model.Events.TurnOf;
import it.polimi.ingsw.psp23.model.Game.*;
import it.polimi.ingsw.psp23.model.Events.EventForSmugglers;
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
    private List<Integer> lostCount = new ArrayList<>();

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
                     List<Item> prize,
                     int id) {
        super(level, id);
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
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
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
            throw new CardException("Cannot load goods in this phase");
        }
        if (!username.equals(winner)) {
            throw new CardException("You are not winner");
        }
        if(loadedCount == prize.size()){
            throw new CardException("You have just awarded prize");
        }
        if (loadedCount == 0) {
            int idx = game.getPlayers().indexOf(game.getPlayerFromNickname(username));
            Utility.updatePosition(Game.getInstance().getPlayers(), idx, -days);
        }
        try {
            Board board = game.getPlayerFromNickname(username).getTruck();
            board.loadGoods(prize.get(loadedCount), i, j);
            loadedCount++;
            if (loadedCount == prize.size()) {
                if (allItemsStolen()) {
                    game.sortPlayersByPosition();
                    game.nextCard();
                }
            }
        }
        catch (InvalidCoordinatesException | ComponentMismatchException | ContainerException |
               TypeMismatchException e){
            throw new ItemException("Caricamento non valido", e);
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
        loadedCount = prize.size();
        if (allItemsStolen()) {
            game.sortPlayersByPosition();
            game.nextCard();
        }
    }

    /**
     * Checks if all items have been stolen from losing players.
     *
     * @return true if each player's lostCount >= numItemsStolen
     */
    private boolean allItemsStolen() {
        Game game = Game.getInstance();
        for (String p : losers) {
            Player player = game.getPlayerFromNickname(p);
            if(player.getTruck().calculateGoods() > 0 || player.getTruck().calculateBatteriesAvailable() > 0) {
                if (lostCount.get(game.getPlayers().indexOf(player)) < numItemsStolen) {
                    return false;
                }
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
     * @param num the index of the item to remove
     * @throws CardException if phase is invalid, wrong player, or removal fails
     */
    public void removePreciousItem(String username, int i, int j, int num) {
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_SMUGGLERS) {
            throw new CardException("Cannot remove goods");
        }
        if (!losers.contains(username)) {
            throw new CardException("You are not loser");
        }
        if(game.getPlayerFromNickname(username).getTruck().calculateGoods() < num){
            throw new CardException("You can only lose batteries");
        }
        try {
            Board board = game.getPlayerFromNickname(username).getTruck();
            board.removePreciousItem(i, j, num);
            int pidx = game.getPlayers().indexOf(game.getPlayerFromNickname(username));
            lostCount.set(pidx, lostCount.get(pidx) + 1);
            if (allItemsStolen() && (winner == null || loadedCount == prize.size())) {
                game.nextCard();
            }
        }
        catch (IllegalArgumentException | ComponentMismatchException | ContainerException |
               TypeMismatchException e){
            throw new ItemException("Scaricamento non valido", e);
        }
    }

    public void removeBatteries(String username, int i, int j, int num){
        Game game = Game.getInstance();
        if (game.getGameStatus() != GameStatus.END_SMUGGLERS) {
            throw new CardException("Cannot remove batteries");
        }
        if (!losers.contains(username)) {
            throw new CardException("You are not loser");
        }
        if(game.getPlayerFromNickname(username).getTruck().calculateGoods() > 0){
            throw new CardException("You have to lose items");
        }
        try {
            Board board = game.getPlayerFromNickname(username).getTruck();
            board.reduceBatteries(i, j, num);
            int pidx = game.getPlayers().indexOf(game.getPlayerFromNickname(username));
            lostCount.set(pidx, lostCount.get(pidx) + num);
            if (allItemsStolen() && (winner == null || loadedCount == prize.size())) {
                game.nextCard();
            }
        }
        catch (InvalidCoordinatesException | ComponentMismatchException | BatteryOperationException |
               TypeMismatchException e){
            throw new ItemException("Perdita batterie non valido", e);
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
        game.setCurrentPlayer(game.getPlayers().getFirst());
    }

    /**
     * Processes the READY command in INIT_SMUGGLERS phase.
     *
     * @param username the player's nickname issuing READY
     * @throws CardException if not INIT_SMUGGLERS
     */
    public void ready(String username) {
        Game game = Game.getInstance();
        if (!game.getCurrentPlayer().getNickname().equals(username)) {
            throw new CardException("Is the turn of " + game.getCurrentPlayer().getNickname());
        }
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
            game.fireEvent(new EnemyDefeated(game.getGameStatus()));
            game.fireEvent(new CosmicCreditsEarned(game.getGameStatus()), username);
            game.setGameStatus(GameStatus.END_SMUGGLERS);
        } else if (power < firePower){
            losers.add(username);
        }
        if (game.getCurrentPlayerIndex() >= game.getPlayers().size() - 1) {
            game.setGameStatus(GameStatus.END_SMUGGLERS);
        } else {
            game.getNextPlayer();
            game.fireEvent(new TurnOf(game.getGameStatus(), game.getCurrentPlayer().getNickname()));
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
                return "Available commands: LOADGOOD, PASS, PERDI, BATTERIE";
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
