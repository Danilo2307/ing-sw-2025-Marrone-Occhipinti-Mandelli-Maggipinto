package it.polimi.ingsw.psp23.model.Game;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.exceptions.HeapIsEmptyException;
import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.exceptions.PlayerNotExistsException;
import it.polimi.ingsw.psp23.exceptions.UncoveredIsEmptyException;
import it.polimi.ingsw.psp23.model.cards.*;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Game {
    private final ArrayList<Player> players;
    private final ArrayList<Player> playersNotOnFlight;
    private final ArrayList<Card> deck;
    private final ArrayList<Card> visibleCards1;
    private final ArrayList<Card> visibleCards2;
    private final ArrayList<Card> visibleCards3;
    private final ArrayList<Component> heap;
    private final ArrayList<Component> uncovered;
    private final int gameId;
    private Player currentPlayer;
    private GameStatus gameStatus;
    private Card currentCard;
    private String deck1Owner;
    private String deck2Owner;
    private String deck3Owner;

    public Game(int gameId) {
        this.players = new ArrayList<>();
        this.playersNotOnFlight = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.heap = new ArrayList<>();
        this.uncovered = new ArrayList<>();
        this.gameId = gameId;
        this.currentPlayer = null;
        this.gameStatus = GameStatus.Setup;
        this.currentCard = null;
        this.visibleCards1 = new ArrayList<>(); //questi sono i tre mazzetti visibili ad inizio game, vanno staccati
        this.visibleCards2 = new ArrayList<>(); //perchè possono essere visti solo uno alla volta
        this.visibleCards3 = new ArrayList<>();
        this.deck1Owner = null;
        this.deck2Owner = null;
        this.deck3Owner = null;

        // per le carte devo avere 2 liste per creare deck corretto: una di livello 1 e una di liv2 e poi faccio shuffle
        ArrayList<Card> level1Cards = CardFactory.generateLevel1Cards();
        ArrayList<Card> level2Cards = CardFactory.generateLevel2Cards();
        Collections.shuffle(level1Cards);
        Collections.shuffle(level2Cards);
        this.visibleCards1.add(level1Cards.get(0));
        this.visibleCards1.addAll(level2Cards.subList(0,2));
        this.visibleCards2.add(level1Cards.get(1));
        this.visibleCards2.addAll(level2Cards.subList(2,4));
        this.visibleCards3.add(level1Cards.get(2));
        this.visibleCards3.addAll(level2Cards.subList(4,6));
        this.deck.addAll(level1Cards.subList(0,4));  // indice finale è escluso
        this.deck.addAll(level2Cards.subList(0,8));
        Collections.shuffle(this.deck);

        // istanzio tutti i componenti e li metto nell'heap
        this.heap.addAll(ComponentFactory.generateAllComponents());
        Collections.shuffle(this.heap);
    }

    /** Player per rimanere in partita deve avere almeno un umano e non essere stato doppiato */
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

    /** ad ogni turno elimina i giocatori non più in volo e riordina la lista in ordine di position decrescente */
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
        for(Player p: players){
            if(p.getNickname().equals(nickname)){
                throw new PlayerExistsException("Player already exists");
            }
        }
        players.add(new Player(nickname));
    }


    public int getGameId(){
        return gameId;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus status){
        gameStatus = status;
    }

    /**
     * Draws a random component from the shared heap (face-down pile) and removes it.
     *
     * @return the randomly selected Component from the heap
     * @throws HeapIsEmptyException if the heap is empty
     */
    public Component getTileFromHeap() throws HeapIsEmptyException {
        // Synchronize on the heap to avoid concurrent modifications
        synchronized (heap) {
            if (heap.isEmpty()) {
                throw new HeapIsEmptyException("No more components available in the heap!");
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
     *
     * @param position index of the tile to be taken
     * @return the selected face-up component
     * @throws UncoveredIsEmptyException if the uncovered list is empty
     * @throws IndexOutOfBoundsException if position is invalid
     */
    public Component getTileUncovered(int position) throws UncoveredIsEmptyException {
        // Synchronize on the uncovered list to avoid race conditions
        synchronized (uncovered) {
            if (uncovered.isEmpty()) {
                throw new UncoveredIsEmptyException("No tiles are face-up!");
            }
            if (position < 0 || position >= uncovered.size()) {
                throw new IndexOutOfBoundsException("Position invalid: " + position);
            }
            Component c = uncovered.get(position);
            uncovered.remove(c);
            // Mark the component as "in hand"
            c.moveToHand();
            return c;
        }
    }

    /**
     * Releases a component to the face-up area, making it available for others to see or pick.
     *
     * @param c the component being discarded face-up
     */
    public void releaseTile(Component c) {
        // Synchronize on the uncovered list to safely add the tile
        synchronized (uncovered) {
            uncovered.add(c);
            c.discardFaceUp();
        }
    }


    public void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

    public Card getCurrentCard(){
        return currentCard;
    }

    public ArrayList<Card> getVisibleDeck1(Player player){
        if(gameStatus == GameStatus.Building && deck1Owner == null && player.getTruck().isWelded()) {
            synchronized (visibleCards1) {
                deck1Owner = player.getNickname();
                return new ArrayList<>(visibleCards1);
            }
        }
        else
            return null;
    }

    public ArrayList<Card> getVisibleDeck2(Player player){
        if(gameStatus == GameStatus.Building && deck2Owner == null && player.getTruck().isWelded()) {
            synchronized (visibleCards2) {
                deck2Owner = player.getNickname();
                return new ArrayList<>(visibleCards2);
            }
        }
        else
            return null;
    }

    public ArrayList<Card> getVisibleDeck3(Player player){
        if(gameStatus == GameStatus.Building && deck3Owner == null && player.getTruck().isWelded()) {
            synchronized (visibleCards3) {
                deck3Owner = player.getNickname();
                return new ArrayList<>(visibleCards3);
            }
        }
        else
            return null;
    }

    public Card getNextCard(){
        int size = deck.size();
        int pos = deck.indexOf(currentCard);
        if(pos+1 < size){
            currentCard = deck.get(pos+1);
            return currentCard;
        }else{
            throw new IndexOutOfBoundsException("Gioco finito");
        }
    }

    public void releaseVisibleDeck1(Player player){
        synchronized (visibleCards1) {
            if (player.getNickname().equals(deck1Owner)) {
                deck1Owner = null;
            } else {
                throw new IllegalStateException("Player " + player.getNickname() + " ha provato a rilasciare un deck non in suo possesso");
            }
        }
    }

    public void releaseVisibleDeck2(Player player){
        synchronized (visibleCards2) {
            if (player.getNickname().equals(deck2Owner)) {
                deck2Owner = null;
            } else {
                throw new IllegalStateException("Player " + player.getNickname() + " ha provato a rilasciare un deck non in suo possesso");
            }
        }
    }

    public void releaseVisibleDeck3(Player player){
        synchronized (visibleCards3) {
            if (player.getNickname().equals(deck3Owner)) {
                deck3Owner = null;
            } else {
                throw new IllegalStateException("Player " + player.getNickname() + " ha provato a rilasciare un deck non in suo possesso");
            }
        }
    }

    public Player getNextPlayer(){
        int size = players.size();
        int pos = players.indexOf(currentPlayer);
        if(pos+1 < size){
            currentPlayer = players.get(pos+1);
            return currentPlayer;
        }else{
            throw new IndexOutOfBoundsException("Giocatori terminati");
        }
    }

    public Player getPlayerFromNickname(String nickname) throws PlayerExistsException {
        for (Player player : players) {
            if(player.getNickname().equals(nickname))
                return player;
        }
        throw new PlayerNotExistsException("Player not found");
    }

    /**
     * Calculates and updates the final score (money) for all players at the end of the game.
     * Applies rewards for arrival order and ship quality, computes profits from goods,
     * and applies penalties for lost components. Players who abandoned the flight
     * receive only half of goods sales and penalties for lost components.
     * */
    public void calculateFinalScores() {

        // lista già ordinata: calcolo ricompense per ordine di arrivo
        int []arrivalPrize = {4, 3, 2, 1};
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
                player.updateMoney(2);
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
    }

}

