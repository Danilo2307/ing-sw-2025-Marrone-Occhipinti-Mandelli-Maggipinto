package it.polimi.ingsw.psp23.model;
import it.polimi.ingsw.psp23.model.cards.*;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.components.*;

import java.util.ArrayList;
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

        // heap creo ComponentFactory in cui istanzio tutto, anche con sottometodo per generare ogni set di component.
        // this.heap.addAll(ComponentFactory.generateAllComponents()); . Questo sarà metodo public static

        // stessa cosa con CardFactory solo che dovrò avere 2 liste: una di livello 1 e una di liv2 e poi faccio shuffle
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

