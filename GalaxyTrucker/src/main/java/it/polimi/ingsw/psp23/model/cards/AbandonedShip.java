package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import java.util.List;

public class AbandonedShip extends Card {
    //Danilo

    private final int days;
    private final int cosmicCredits;
    private final int numMembers;

    AbandonedShip(int level,int days, int cosmicCredits, int numMembers) {
        super(level);
        this.days = days;
        this.cosmicCredits = cosmicCredits;
        this.numMembers = numMembers;
    }

    public int getDays() {
        return days;
    }

    public int getCosmicCredits() {
        return cosmicCredits;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void play(List<Player> players){
        boolean isSold = false;
        int size = players.size();
        int i = 0;
        while(i < size && !isSold){
            if(players.get(i).getTruck().calculateCrew() > numMembers && players.get(i).isInGame()) {
                isSold = true; // qui ricevo la risposta del player e la metto nella variabile isSold
                if (isSold) {
                   //verrà anche deciso da quali hub togliere i membri da eliminare
                    players.get(i).getTruck().reduceCrew(numMembers,1,1);
                    players.get(i).updateMoney(cosmicCredits);
                    Utility.updatePosition(players,i,-days);
                    i = size; //al pari di un break
                }
                i++;
            }
        }
    }
}
