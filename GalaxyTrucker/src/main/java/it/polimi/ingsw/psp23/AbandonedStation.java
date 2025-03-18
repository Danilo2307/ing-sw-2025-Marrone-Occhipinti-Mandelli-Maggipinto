package it.polimi.ingsw.psp23;

import java.util.List;

public class AbandonedStation extends Card {
//GIGI
    private final int days;
    private final int numMembers;
    private final List<Item[]> prize;

    public AbandonedStation(int level, int days, int numMembers, List<Item[]> prize) {
        super(level);
        this.days = days;
        this.numMembers = numMembers;
        this.prize = prize;
    }

    public void play(List<Player> players) {
        for (Player player : players) {
            if (player.isInGame() && player.getTruck().getCrewSize() >= numMembers) {
                // Optional: conferma del giocatore (interazione UI)
                if (playerWantsToAccept(player)) {
                    player.getTruck().loadGoods(prize);
                    player.updatePosition(-days);
                    break; // solo uno può accettare
                }
            }
        }
    }

    // Simulazione di decisione (da sostituire con input reale)
    //private boolean playerWantsToAccept(Player player) {
        // TODO: logica reale di interazione
       // return true;
   // }
}
