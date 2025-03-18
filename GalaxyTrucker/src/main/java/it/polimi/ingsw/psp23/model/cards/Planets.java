package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;

import java.util.List;

public class Planets extends Card {
    private final int daysLost;
    private final List<Item[]> planetGoods;
    //Federico
    Planets (int level, int daysLost, List<Item[]> planetGoods) {
        super(level);
        this.daysLost = daysLost;
        this.planetGoods = planetGoods;
    }

    @Override
    public void play(List<Player> players) {
        int i = 0;
        // di default Java li inizializza tutti a false, come serve a me
        // array che mi dicono se pianeta i-esimo è occupato e se player i-esimo è atterrato
        boolean[] planetOccupied = new boolean[planetGoods.size()];
        boolean[] playerLanded = new boolean[players.size()];

        while (i < players.size()) {
            // chosenPlanet è l'indice del pianeta su cui il giocatore vorrebbe atterrare
            int chosenPlanet = players.get(i).decide();
            // se chosenPlanet non è nell'intervallo sensato significa che il player corrente non vuole atterrare.
            // Se utente per errore sceglie pianeta occupato non gli viene data un'altra chance
            if (chosenPlanet >= 1 && chosenPlanet <= planetGoods.size() && !planetOccupied[chosenPlanet-1]) {
                    planetOccupied[chosenPlanet-1] = true;  // -1 perchè suppongo l'utente inserisca 1-based mentre array 0-based
                    players.get(i).getTruck().loadGoods(planetGoods.get(chosenPlanet-1));
                    playerLanded[i] = true;
            }
            i++;
        }
        // aggiorno le posizioni in ordine inverso di posizione di rotta
        for (i = players.size() - 1 ; i >= 0 ; i--) {
            if (playerLanded[i]) {
                players.get(i).updatePosition(-daysLost);
            }
        }
    }
}


