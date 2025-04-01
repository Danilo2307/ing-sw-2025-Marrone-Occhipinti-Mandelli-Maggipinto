package it.polimi.ingsw.psp23.model.Game;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.cards.*;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.ComponentLocation;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Game {
    private ArrayList<Player> players;
    private ArrayList<Player> playersNotOnFlight;
    private ArrayList<Card> deck;
    private ArrayList<Card> visibleCards1;
    private ArrayList<Card> visibleCards2;
    private ArrayList<Card> visibleCards3;
    private ArrayList<Component> heap;
    private ArrayList<Component> uncovered;
    private int gameId;
    private Player currentPlayer;
    private GameStatus  gameStatus;
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
        // creo e aggiungo nel mucchio tutti i component della nave
        this.heap.addAll(ComponentFactory.generateAllComponents());
        this.currentPlayer = null;
        this.gameStatus = GameStatus.Idle;
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
        this.visibleCards3.add(level1Cards.get(3));
        this.visibleCards3.addAll(level2Cards.subList(4,6));
        this.deck.addAll(level1Cards.subList(0,4));  // indice finale è escluso
        this.deck.addAll(level2Cards.subList(0,8));
        Collections.shuffle(this.deck);

        this.heap = ComponentFactory.generateAllComponents();
        Collections.shuffle(this.heap);

    }

    // riordina la lista ad ogni turno
    public void sortPlayersByPosition() {

        // se player è uscito al turno corrente, lo levo dalla lista dei player correnti.
        // Lo salvo in un'altra lista perchè partecipa comunque alla conclusione del viaggio
        for (Player player : players) {
            if (!player.isInGame()) {
                playersNotOnFlight.add(player);
                players.remove(player);
            }
        }
        players.sort(Comparator.comparingInt(Player::getPosition).reversed());
        //check
    }

    public void removeFromGame(Player player) {
        playersNotOnFlight.add(player);
        players.remove(player);
    }

    public void addPlayer(String nickname) {
        for(Player p: players){
            if(p.getNickname().equals(nickname)){
                throw new RuntimeException("Player already exists");
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

   // public Timer getTimer(){
  //      return Timer;
  //  }


    public Component getTileFromHeap(){
        Component c =  heap.get(Utility.randomComponent(heap.size()));
        synchronized (c) {
            if (c.getState() == ComponentLocation.PILE) {
                c.moveToHand();
                return c;
            }
            else
                throw new RuntimeException("Errore, componente non disponibile");
        }
    }


    public Component getTileUncovered(int position){
        Component c =  heap.get(Utility.randomComponent(heap.size()));
        synchronized (c) {
            if (c.getState() == ComponentLocation.FACE_UP) {
                c.moveToHand();
                return c;
            }
            else
                throw new RuntimeException("Errore, componente non disponibile");
        }
    }

    public void addTileUncovered(Component component){
        component.discardFaceUp();
        uncovered.add(component);
    }

    public void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

    public Card getCurrentCard(){
        return currentCard;
    }

    public ArrayList<Card> getVisibleDeck1(Player player){
        if(gameStatus == GameStatus.Build && deck1Owner == null && player.getTruck().isWelded()) {
            synchronized (visibleCards1) {
                deck1Owner = player.getNickname();
                return new ArrayList<>(visibleCards1);
            }
        }
        else
            return null;
    }

    public ArrayList<Card> getVisibleDeck2(Player player){
        if(gameStatus == GameStatus.Build && deck2Owner == null && player.getTruck().isWelded()) {
            synchronized (visibleCards2) {
                deck2Owner = player.getNickname();
                return new ArrayList<>(visibleCards2);
            }
        }
        else
            return null;
    }

    public ArrayList<Card> getVisibleDeck3(Player player){
        if(gameStatus == GameStatus.Build && deck3Owner == null && player.getTruck().isWelded()) {
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



}

