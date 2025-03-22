package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import java.util.List;

public class AbandonedStation extends Card {

    private final int days;
    private final int numMembers;
    private final List<Item> prize;

    public AbandonedStation(int level, int days, int numMembers, List<Item> prize) {
        super(level);
        this.days = days;
        this.numMembers = numMembers;
        this.prize = prize;
    }

    @Override
    public void play(List<Player> players) {
        for (Player player : players) {
            if (player.isInGame() && player.getTruck().calculateCrew() >= numMembers) {
                // Optional: conferma del giocatore (interaction UI)
                if (playerWantsToAccept(player)) {
                    for (Item item : prize) {
                        // TODO: ottenere x, y del container per ogni merce dal giocatore con interazione UI
                        int i = 0; // placeholder
                        int j = 0; // placeholder
                        player.getTruck().loadGoods(item, i, j);
                    }
                    Utility.updatePosition(players, players.indexOf(player), -days);
                    break;
                }
            }
        }
    }

    // Placeholder per interazione reale
    private boolean playerWantsToAccept(Player player) {
        // TODO: implementare logica di input utente
        return true;
    }
}
