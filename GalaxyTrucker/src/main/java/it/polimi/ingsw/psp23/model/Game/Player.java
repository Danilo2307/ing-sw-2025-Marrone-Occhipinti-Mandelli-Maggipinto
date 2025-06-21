package it.polimi.ingsw.psp23.model.Game;

import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.Color;


/**
 * Represents a player participating in a game. Each player has their own nickname,
 * position, allocated truck (board), a certain amount of money, and can optionally
 * handle tiles (components). The player is represented by a unique color (the color of his
 * starting housing unit) and can interact with the game session they are part of.
 */
public class Player {
    private final String nickname;
    private int position ;
    private final Board truck;
    private int money;
    private Component currentTileInHand;
    private boolean inGame;
    private Game game;
    private Color color;

    public Player(String nickname, Game game) {
        this.nickname = nickname;
        this.position = 0;
        this.game = game;
        this.truck = new Board(game.getLevel(), nickname);
        this.money = 0;
        this.currentTileInHand = null;
        this.inGame = true;
        this.color = null;

    }

    /**
     * Setta il game a cui il player sta partecipando
     * @game è il riferimento alla sessione di gioco a cui il player partecipa
     */
    public void setGame(Game game){
        this.game = game;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public Board getTruck() {
        return truck;
    }

    /**
     * Updates the player's money by applying a variation to the current amount.
     * Negative values can be passed to represent a monetary loss.
     *
     * @param moneyVariation the amount to add or subtract from the player's current money
     */
    public void updateMoney(int moneyVariation) {
        this.money += moneyVariation;
    }

    public void setPosition(int offset) {
        position = position + offset;
    }

    public int getPosition() {
        return position;
    }

    /**
     * Allows the player to leave the flight while remaining eligible for the final score calculation.
     * Once this method is invoked, the player is no longer considered an active participant in the game.
     * The player will be moved from the "on-flight" group to the "not-on-flight" group within the game
     * through the sort() method provided in the Game class.
     */
    // player abbandona il volo, ma parteciperà al calcolo del punteggio finale
    public void leaveFlight() {
        this.inGame = false;
    }

    public String getNickname() {
        return nickname;
    }

    public int getMoney() {
        return money;
    }

    /**
     * Allows the player to draw a tile from the heap and set it as the current tile in hand.
     * The method ensures that the player does not already have a tile in hand before drawing a new one.
     * If the heap is empty, or drawing is not allowed, exceptions may be propagated to the controller.
     *
     * @return the component drawn from the heap and set as the player's current tile in hand
     * @throws InvalidComponentActionException if the player already has a tile in hand
     */
    public Component chooseTileFromHeap() {
        if (currentTileInHand != null)
            throw new InvalidComponentActionException("Per pescare non devi avere niente già in mano!");
        currentTileInHand = game.getTileFromHeap();
        return currentTileInHand;
    }


    /**
     * Allows the player to select and take a face-up tile from the uncovered list at the specified position and version.
     * Ensures that the player does not already have a tile in hand before drawing a new one.
     * Exceptions, if any, are propagated for handling by the controller.
     *
     * @param position the index of the tile to be taken from the uncovered list
     * @param version the version of the uncovered list the player was viewing when the selection was made
     * @return the component selected and taken from the uncovered list, set as the player's current tile in hand
     * @throws InvalidComponentActionException if the player already has a tile in hand
     */
    public Component chooseCardUncovered(int position, int version) {
        if (currentTileInHand != null)
            throw new InvalidComponentActionException("Per pescare non devi avere niente già in mano!");
        currentTileInHand = game.getTileUncovered(position, version);
        return currentTileInHand;
    }

    /**
     * Discards the component currently held by the player, releasing it back into the game.
     *
     * @throws InvalidComponentActionException if player has no tile in hand or tries to discard a reserved tile
     */
    public void discardComponent(){
        if (currentTileInHand == null)
            throw new InvalidComponentActionException("Non puoi rilasciare nulla perchè non hai niente in mano!");
        if (getTruck().getReservedTiles().contains(currentTileInHand))
            throw new InvalidComponentActionException("Non puoi rilasciare una carta prenotata!");
        game.releaseTile(currentTileInHand);
        currentTileInHand = null;
    }

    /**
     * Rotates the tile currently held by the player in their hand by 90 degrees clockwise.
     * This operation assumes that the player has a tile in hand; otherwise, an exception is thrown.
     *
     * Exceptions:
     * - InvalidComponentActionException: Thrown if the player attempts to rotate a tile
     *   when they do not currently hold one.
     */
    public void rotateTileInHand() {
        if (currentTileInHand == null)
            throw new InvalidComponentActionException("Puoi ruotare solo se hai qualcosa in mano!");
        currentTileInHand.rotate();
    }

    /**
     * Adds the current tile held by the player to the given position on the truck (player's board).
     *
     * The method ensures that the tile currently in hand is placed at the specified coordinates 
     * on the truck. If no tile is currently in hand, an exception is thrown. 
     * Once the tile is added, the player's hand is cleared, setting the current tile in hand to null.
     *
     * @param x the x-coordinate on the truck where the tile is to be placed
     * @param y the y-coordinate on the truck where the tile is to be placed
     * @throws InvalidComponentActionException if the player doesn't have a tile in hand
     */
    public void addTile(int x, int y) {
        if (currentTileInHand == null)
            throw new InvalidComponentActionException("Non hai in mano nulla --> non puoi saldare nulla sulla nave!");
        getTruck().addComponent(currentTileInHand, x, y);
        currentTileInHand = null;
    }

    /**
     * Reserves the current tile in hand, adding it to the player's reserved tiles.
     * Not allowed in level 0.
     *
     * @throws LevelException if game level is 0
     * @throws InvalidComponentActionException if reserving tiles exceeds allowed maximum
     */
    public void reserve() {
        if (game.getLevel() == 0)
            throw new LevelException("Non puoi prenotare tessere nel livello di prova!");

        getTruck().reserveTile(currentTileInHand);
        currentTileInHand = null;
    }


    /**
     * Takes a reserved tile by index and sets it as current tile in hand.
     * Not allowed in level 0.
     *
     * @param index index of the reserved tile to take
     * @throws LevelException if game level is 0
     */
    public void takeReservedTile(int index) {
        if (game.getLevel() == 0)
            throw new LevelException("Non puoi prenotare tessere nel livello di prova!");

        Component c = getTruck().getReservedTiles().get(index);
        currentTileInHand = c;
        c.moveToHand();
    }

    public Component getCurrentTileInHand() {
        return currentTileInHand;
    }

    public void setColor(Color colore) {
        this.color = colore;
    }

    public Color getColor() {
        return color;
    }
}
