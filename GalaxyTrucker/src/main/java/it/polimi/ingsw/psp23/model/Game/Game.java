package it.polimi.ingsw.psp23.model.Game;
import it.polimi.ingsw.psp23.model.cards.*;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.components.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Game {
    private ArrayList<Player> players;
    private ArrayList<Player> playersNotOnFlight;
    private ArrayList<Card> deck;
    private ArrayList<Component> heap;
    private ArrayList<Component> uncovered;

    public Game() {
        this.players = new ArrayList<>();
        this.playersNotOnFlight = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.heap = new ArrayList<>();
        this.uncovered = new ArrayList<>();

        // creo e aggiungo nel mucchio tutti i component della nave
        this.heap.addAll(ComponentFactory.generateAllComponents());

        // per le carte devo avere 2 liste per creare deck corretto: una di livello 1 e una di liv2 e poi faccio shuffle
        ArrayList<Card> level1Cards = CardFactory.generateLevel1Cards();
        ArrayList<Card> level2Cards = CardFactory.generateLevel2Cards();
        Collections.shuffle(level1Cards);
        Collections.shuffle(level2Cards);
        this.deck.addAll(level1Cards.subList(0,4));  // indice finale è escluso
        this.deck.addAll(level2Cards.subList(0,8));
        Collections.shuffle(this.deck);

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


    




}

