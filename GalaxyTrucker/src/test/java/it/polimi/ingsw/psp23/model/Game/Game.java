package it.polimi.ingsw.psp23.model.Game;
import it.polimi.ingsw.psp23.controller.Controller;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Events.TurnOf;
import it.polimi.ingsw.psp23.model.cards.*;
import it.polimi.ingsw.psp23.model.cards.visitor.InitPlayVisitor;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.FinalRanking;
import it.polimi.ingsw.psp23.protocol.response.NewCardDrawn;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * Represents a Game object where players interact with components, cards, and decks
 * to fulfill game logic and mechanics. The Game class manages the players' states,
 * the game board, phases, scoring, and events during a game session.
 */
public class Game {

    private final ArrayList<Player> players;
    private final ArrayList<Player> playersNotOnFlight;
    private final ArrayList<Card> deck;
    private ArrayList<Card> visibleCards1 = null;
    private ArrayList<Card> visibleCards2 = null;
    private ArrayList<Card> visibleCards3 = null;
    private final ArrayList<Component> heap;
    private final ArrayList<Component> uncovered;
    private int lastUncoveredVersion;
    private Player currentPlayer;
    private int currentPlayerIndex; // Questa variabile sarà inizializzata a -1
    private GameStatus gameStatus;
    private Card currentCard;
    private String deck1Owner = null;
    private String deck2Owner = null;
    private String deck3Owner = null;
    private Consumer<Event> eventListener;
    private BiConsumer<Event, String> eventListener2;
    private int numRequestedPlayers;
    private int turn;
    int level;
    int []firstPositions;
    int Id;
    Controller controller;

    // This constructor is public because it needs to be accessible from Server which is in another package
    public Game(int level, int id) {
        this.players = new ArrayList<>();
        this.playersNotOnFlight = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.heap = new ArrayList<>();
        this.uncovered = new ArrayList<>();
        this.lastUncoveredVersion = 0;
        this.currentPlayer = null;
        this.currentPlayerIndex = -1;
        this.gameStatus = GameStatus.Setup;
        this.currentCard = null;
        this.numRequestedPlayers = -1;
        this.turn = 0;
        this.level = level;
        this.Id = id;
        this.controller = null;
        // instantiate all components and add them to the heap
        this.heap.addAll(ComponentFactory.generateAllComponents(level));

        if (level == 2) {
            this.visibleCards1 = new ArrayList<>(); //these are the three visible decks visibile during the first phase of the game, they must be detached
            this.visibleCards2 = new ArrayList<>(); //because these are only visible one at a time
            this.visibleCards3 = new ArrayList<>();

            // Create 2 lists for generating correct deck: one card of level 1 and two cards of level 2, then shuffle
            ArrayList<Card> level1Cards = CardFactory.generateLevel1Cards();
            ArrayList<Card> level2Cards = CardFactory.generateLevel2Cards();
            Collections.shuffle(level1Cards);
            Collections.shuffle(level2Cards);
            this.visibleCards1.add(level1Cards.get(0));
            this.visibleCards1.addAll(level2Cards.subList(0, 2));
            this.visibleCards2.add(level1Cards.get(1));
            this.visibleCards2.addAll(level2Cards.subList(2, 4));
            this.visibleCards3.add(level1Cards.get(2));
            this.visibleCards3.addAll(level2Cards.subList(4, 6));
            this.deck.addAll(level1Cards.subList(0, 4));
            this.deck.addAll(level2Cards.subList(0, 8));

            firstPositions = new int[]{8, 5, 3, 2};
        }
        else {
            this.deck.addAll(CardFactory.generateTrialCards());
            firstPositions = new int[]{6, 4, 3, 2};
        }

        Collections.shuffle(this.deck);
        Collections.shuffle(this.heap);

    }

    public int getLevel() {
        return level;
    }


    /**
     * Sets the number of players requested for the game. This value can only be set once.
     * If the number of players has already been defined, an exception will be thrown.
     *
     * @param number the number of players requested for the game
     * @throws InvalidActionException if the number of requested players has already been set
     */
    public void setNumRequestedPlayers(int number) {
        if (numRequestedPlayers == -1)
            this.numRequestedPlayers = number;
        else
            throw new InvalidActionException("Non puoi modificare il numero di giocatori attesi!");
    }

    /**
     * Retrieves the number of players requested for the game.
     *
     * @return the number of players requested for the game
     */
    public int getNumRequestedPlayers() {
        return numRequestedPlayers;
    }

    /**
     * Retrieves the earnings based on the player’s final ranking.
     *
     * @return an array of integers representing the first positions
     */
    public int[] getFirstPositions() {
        return firstPositions;
    }

    /**
     * This method checks the status of all players in the game to determine whether they should
     * be eliminated from the flight. A player is eliminated from the flight if either:
     * - Their truck's human crew count is zero.
     * - The gap between their position and the leading player's position exceeds 24.
     *
     * The method assumes that `maxPosition` refers to the position of the leading player,
     * even if they are eliminated during the process. It does not update `maxPosition` dynamically
     * in such cases.
     */

    public void checkEliminationPlayers() {
        // ricavo posizione del leader
        int maxPosition = players.stream().mapToInt(Player::getPosition).max().orElse(0);

        for (Player player : players) {
            // check umani
            if (player.getTruck().calculateHumanCrew() == 0)
                player.leaveFlight();
            // check doppiaggio
            if (maxPosition - player.getPosition() > 24)
                player.leaveFlight();
        }
        // OSS: se elimino il leader non riaggiorno maxPosition: potremmo supporre che si faccia riferimento sempre a lui anche se eliminato (non specificato su regolamento)
    }

    /**
     * Sorts the list of players currently in the game based on their positions.
     *
     * This method performs the following actions:
     * 1. Identifies and removes players who are no longer in the game. These players are transferred to another list for players who are not in the flight.
     * 2. Sorts the remaining players in descending order of their position values.
     *
     * Players who are not in the game are determined using the `isInGame()` method on each player object.
     * The sorting process uses the `getPosition()` method of the `Player` class and orders players by their position in descending order.
     */
    public void sortPlayersByPosition() {
        // necessaria perchè non posso rimuovere un oggetto dalla stessa lista su cui sto iterando tramite for-each
        List<Player> toRemove = new ArrayList<>();
        // se player è uscito al turno corrente, lo levo dalla lista dei player correnti.
        // Lo salvo in un'altra lista perchè partecipa comunque alla conclusione del viaggio
        for (Player player : players) {
            if (!player.isInGame()) {
                playersNotOnFlight.add(player);
                toRemove.add(player);
            }
        }
        players.removeAll(toRemove);

        players.sort(Comparator.comparingInt(Player::getPosition).reversed());
    }

    public void addPlayer(String nickname) {
        players.add(new Player(nickname, this));
    }

    public ArrayList<Component> getUncovered() {
        synchronized (uncovered) {
            return uncovered;
        }
    }

    public int getLastUncoveredVersion() {
        synchronized (uncovered) {
            return lastUncoveredVersion;
        }
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public ArrayList<Player> getPlayersNotOnFlight() {
        return playersNotOnFlight;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus status){
        gameStatus = status;
    }

    /**
     * Draws a random component from the shared heap (face-down pile) and removes it.
     * @return the randomly selected Component from the heap
     * @throws NoTileException if the heap is empty
     */
    public Component getTileFromHeap() {
        // Synchronize on the heap to avoid concurrent modifications
        synchronized (heap) {
            if (heap.isEmpty()) {
                throw new NoTileException("No more tiles available in the heap! Pick from the uncovered");
            }
            // Pick a random index and remove that component from the heap
            Component c = heap.get(Utility.randomComponent(heap.size()));
            heap.remove(c);
            // Mark the component as "in hand"
            c.moveToHand();
            return c;
        }
    }

    /**
     * Takes a face-up component from the uncovered list at the position chosen by the player, removing it from that list.
     * @param position index of the tile to be taken
     * @param version of the uncoveredList that the user was seeing when he decided to draw the 'position' tile
     * @return the selected face-up component
     * @throws NoTileException if the uncovered list is empty
     * @throws IndexOutOfBoundsException if position is invalid
     */
    public Component getTileUncovered(int position, int version)  {
        // Synchronize on the uncovered list to avoid race conditions
        synchronized (uncovered) {
            if (uncovered.isEmpty()) {
                throw new NoTileException("No tiles are face-up! Pick from the heap");
            }
            if (position < 0 || position >= uncovered.size()) {
                throw new IndexOutOfBoundsException("Position invalid: " + position);
            }
            // another player has drawn from the list --> what the player was seeing is not up to date
            if (version != lastUncoveredVersion)
                throw new NoTileException("La lista di tessere scoperte che vedi non è aggiornata.\nEcco la versione aggiornata:\n");
            Component c = uncovered.get(position);
            uncovered.remove(c);
            lastUncoveredVersion++;
            // Mark the component as "in hand"
            c.moveToHand();
            return c;
        }
    }

    /**
     * Releases a component to the face-up area, making it available for others to see or pick.
     * @param c the component being discarded face-up
     */
    public void releaseTile(Component c) {
        // Synchronize on the uncovered list to safely add the tile
        synchronized (uncovered) {
            uncovered.add(c);
            c.discardFaceUp();
        }
    }

    /**
     * Checks and updates the number of reserved tiles for each player in the game.
     *
     * For every player:
     * - Determines the number of tiles reserved in the player's truck.
     * - If the truck has reserved tiles, updates the truck's garbage value with the count of reserved tiles.
     *
     * This method iterates through all players in the game and modifies relevant player data accordingly.
     */
    public void checkReservedTiles() {
        for (Player p : players) {
            int reserved = p.getTruck().getReservedTiles().size();
            if (reserved != 0) {
                p.getTruck().setGarbage(reserved);
            }
        }
    }

    /** @return the current player in the round */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }

    /** @param player the player to set as current */
    public void setCurrentPlayer(Player player){
        currentPlayer = player;
        currentPlayerIndex = players.indexOf(player);
        if(gameStatus != GameStatus.Building && gameStatus != GameStatus.Setup && gameStatus != GameStatus.CheckBoards && gameStatus != GameStatus.SetCrew) {
            fireEvent(new TurnOf(getGameStatus(), currentPlayer.getNickname()));
        }
    }

    /** @return the next player in the round, or null if the round is over        */
    public Player getNextPlayer(){
        int size = players.size();
        if(currentPlayerIndex < size - 1){
            turn ++;
            setCurrentPlayer(players.get(currentPlayerIndex+1));
            return currentPlayer;
        } else{
            return null;
        }
    }

    public int getTurn(){
        return turn;
    }

    /**
     * Retrieves a player by their nickname from the list of players in the game.
     * If the player with the specified nickname is not found, an exception is thrown.
     *
     * @param nickname the nickname of the player to retrieve
     * @return the Player object corresponding to the specified nickname
     * @throws PlayerNotExistsException if no player with the given nickname exists in the game
     */
    public Player getPlayerFromNickname(String nickname) throws PlayerNotExistsException {
        for (Player player : players) {
            if(player.getNickname().equals(nickname))
                return player;
        }
        throw new PlayerNotExistsException("Player not found");
    }

    /** @return the current card */
    public Card getCurrentCard(){
        return currentCard;
    }

    /** @return the next card in the deck, or null if every card has been played */
    public Card getNextCard(){
        int size = deck.size();
        if(currentCard == null) return deck.getFirst();
        int pos = deck.indexOf(currentCard);
        if(pos+1 < size){
            currentCard = deck.get(pos+1);
            return currentCard;
        }else{
            return null;
        }
    }

    /**
     * Executes the logic to proceed to the next card in the game. It performs several actions including:
     * eliminating players who do not meet certain criteria, sorting the players by their position,
     * drawing the next card from the deck, and updating the game status.
     *
     * If no more cards are available, or there is only one player remaining, the game will end,
     * final scores will be calculated, and the winners will be announced. Otherwise, the game
     * continues and the new card's logic is executed.
     *
     * @param username the username of the player currently involved in the card action
     */
    public void nextCard(String username){
        checkEliminationPlayers();
        sortPlayersByPosition();
        currentCard = getNextCard();
        if(currentCard == null || players.size() <= 1){
            setGameStatus(GameStatus.End);
            calculateFinalScores();

            // send final ranking to clients
            List<AbstractMap.SimpleEntry<String, Integer>> ranking = new ArrayList<>();
            for (Player p: players) {
                ranking.add(new AbstractMap.SimpleEntry<>(p.getNickname(), p.getMoney()));
            }
            for (Player p: playersNotOnFlight) {
                ranking.add(new AbstractMap.SimpleEntry<>(p.getNickname(), p.getMoney()));
            }
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new FinalRanking(ranking)), Id);

        }else{
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new NewCardDrawn(currentCard.getId(), currentCard.toString())), Id);
            VisitorUsername visitor = new InitPlayVisitor();
            currentCard.call(visitor, username);
        }
    }

    /** Allows a player to temporarily view a deck during the Building phase.
     * Access is granted only if:
     *  - the game is in Building phase
     *  - no other player is currently viewing this deck
     *  - the player has at least one tile on the truck (isWelded)
     * @param player who wants to see the deck1
     * @return a copy of the visible deck if access is granted, otherwise null
     */
    public ArrayList<Card> getVisibleDeck1(Player player){
        if (level == 0) {
            throw new LevelException("Non esistono mazzetti visibili nel volo di prova!");
        }

        String nickname = player.getNickname();
        synchronized (visibleCards1) {
            if (gameStatus == GameStatus.Building && deck1Owner == null && player.getTruck().isWelded() && (deck2Owner == null || !deck2Owner.equals(nickname)) && (deck3Owner == null || !deck3Owner.equals(nickname))) {
                deck1Owner = nickname;
                return new ArrayList<>(visibleCards1);
            } else
                return null;
        }
    }

    /** Allows a player to temporarily view a deck during the Building phase.
     * Access is granted only if:
     *  - the game is in Building phase
     *  - no other player is currently viewing this deck
     *  - the player has at least one tile on the truck (isWelded)
     * @param player who wants to see the deck2
     * @return a copy of the visible deck if access is granted, otherwise null
     */
    public ArrayList<Card> getVisibleDeck2(Player player){
        if (level == 0) {
            throw new LevelException("Non esistono mazzetti visibili nel volo di prova!");
        }

        String nickname = player.getNickname();
        synchronized (visibleCards2) {
            if(gameStatus == GameStatus.Building && deck2Owner == null && player.getTruck().isWelded() && (deck3Owner == null || !deck3Owner.equals(nickname)) && (deck1Owner == null || !deck1Owner.equals(nickname))) {
                deck2Owner = nickname;
                return new ArrayList<>(visibleCards2);
            }
            else
                return null;
        }
    }

    /** Allows a player to temporarily view a deck during the Building phase.
     * Access is granted only if:
     *  - the game is in Building phase
     *  - no other player is currently viewing this deck
     *  - the player has at least one tile on the truck (isWelded)
     * @param player who wants to see the deck3
     * @return a copy of the visible deck if access is granted, otherwise null
     */
    public ArrayList<Card> getVisibleDeck3(Player player) {
        if (level == 0) {
            throw new LevelException("Non esistono mazzetti visibili nel volo di prova!");
        }

        String nickname = player.getNickname();
        synchronized (visibleCards3) {
            if (gameStatus == GameStatus.Building && deck3Owner == null && player.getTruck().isWelded() && (deck1Owner == null || !deck1Owner.equals(nickname)) && (deck2Owner == null || !deck2Owner.equals(nickname))) {
                deck3Owner = nickname;
                return new ArrayList<>(visibleCards3);
            }
            else
                return null;
        }
    }

    /** releases the first deck after the player has finished viewing it
     * @param player releasing the deck
     * @throws IllegalStateException if the player is not the current owner
     */
    public void releaseVisibleDeck1(Player player){
        if (level == 0) {
            throw new LevelException("Non esistono mazzetti visibili nel volo di prova!");
        }

        synchronized (visibleCards1) {
            if (player.getNickname().equals(deck1Owner)) {
                deck1Owner = null;
            } else {
                throw new InvalidActionException("Player " + player.getNickname() + " ha provato a rilasciare un deck non in suo possesso");
            }
        }
    }

    /** releases the second deck after the player has finished viewing it
     * @param player releasing the deck
     * @throws IllegalStateException if the player is not the current owner
     */
    public void releaseVisibleDeck2(Player player){
        if (level == 0) {
            throw new LevelException("Non esistono mazzetti visibili nel volo di prova!");
        }

        synchronized (visibleCards2) {
            if (player.getNickname().equals(deck2Owner)) {
                deck2Owner = null;
            } else {
                throw new InvalidActionException("Player " + player.getNickname() + " ha provato a rilasciare un deck non in suo possesso");
            }
        }
    }

    /** releases the third deck after the player has finished viewing it
     * @param player releasing the deck
     * @throws IllegalStateException if the player is not the current owner
     */
    public void releaseVisibleDeck3(Player player){
        if (level == 0) {
            throw new LevelException("Non esistono mazzetti visibili nel volo di prova!");
        }

        synchronized (visibleCards3) {
            if (player.getNickname().equals(deck3Owner)) {
                deck3Owner = null;
            } else {
                throw new InvalidActionException("Player " + player.getNickname() + " ha provato a rilasciare un deck non in suo possesso");
            }
        }
    }


    /**
     * Calculates and updates the final score (money) for all players at the end of the game.
     * Applies rewards for arrival order and ship quality, computes profits from goods,
     * and applies penalties for lost components. Players who abandoned the flight
     * receive only half of goods sales and penalties for lost components.
     * */
    public void calculateFinalScores() {
        int []arrivalPrize;
        int bestShipPrize;
        if (level == 0) {
            arrivalPrize = new int[]{4, 3, 2, 1};
            bestShipPrize = 2;
        }
        else {
            arrivalPrize = new int[]{8, 6, 4, 2};
            bestShipPrize = 4;
        }

        // ordino lista e calcolo ricompense per ordine di arrivo
        sortPlayersByPosition();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).updateMoney(arrivalPrize[i]);
        }

        // ricompensa nave più bella (meno connettori esposti). In caso di pareggio, tutti i giocatori ricevono la ricompensa
        int minExposedConnectors = players.stream().mapToInt(p -> p.getTruck().calculateExposedConnectors()).min().orElse(0);

        // sfrutto lo stesso for: calcolo vendita merci, perdite componenti persi e nave più bella
        for (Player player : players) {
            int sales = player.getTruck().calculateGoodsSales();
            player.updateMoney(sales);
            int lostComponents = player.getTruck().getGarbage();
            player.updateMoney(-lostComponents);
            if (player.getTruck().calculateExposedConnectors() == minExposedConnectors)
                player.updateMoney(bestShipPrize);
        }

        // considero giocatori che hanno abbandonato la corsa: non partecipano all'ordine di arrivo e alla nave più bella.
        // Tuttavia, possono vendere le loro merci a metà del prezzo e devono comunque pagare la penale.
        for (Player player : playersNotOnFlight) {

            int sales = player.getTruck().calculateGoodsSales();
            // arrotondamento per eccesso
            int halfSalesRoundedUp = (sales + 1) / 2;
            player.updateMoney(halfSalesRoundedUp);

            int lostComponents = player.getTruck().getGarbage();
            player.updateMoney(-lostComponents);
        }
        players.sort(Comparator.comparingInt(Player::getMoney).reversed());

    }

    public void setEventListener(Consumer<Event> listener) {
        this.eventListener = listener;
    }

    public void setEventListener2(BiConsumer<Event, String> listener) {
        this.eventListener2 = listener;
    }

    /**
     * Triggers the provided event by notifying the associated event listener in the controller.
     *
     * @param event the event to be fired, containing details to notify the listener
     */
    public void fireEvent(Event event) {
        if (eventListener != null) {
            //eventListener.accept(event);
        }
    }

    /**
     * Triggers the specified event by notifying the associated event listener in the controller.
     * This method provides additional context by including the username of the player associated
     * with the event.
     *
     * @param event the event to be fired, containing details to notify the listener
     * @param username the username of the player involved in the event
     */
    public void fireEvent(Event event, String username) {
        if(eventListener != null) {
            //eventListener2.accept(event, username);
        }
    }

    public int getId(){
        return Id;
    }

    public Controller getController() {
        return controller;
    }

    public void setController() {
        this.controller = new Controller(Id);
    }

}

