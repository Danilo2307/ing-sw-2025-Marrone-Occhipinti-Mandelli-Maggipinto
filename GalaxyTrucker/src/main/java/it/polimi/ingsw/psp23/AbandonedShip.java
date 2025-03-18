package it.polimi.ingsw.psp23;
import java.util.List;

public class AbandonedShip extends Card{
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

    public void play(List<Player> Players){
        boolean isSold = false;
        int size = Players.size();
        int i = 0;
        while(i < size && !isSold){
            if(Players.get(i).getTruck().crew > numMembers && Players.get(i).isInGame) {
                isSold = true; // qui ricevo la risposta del player e la metto nella variabile isSold
                if (isSold) {
                   //in reducehubcrew verrà anche deciso da quali hub togliere i membri da eliminare
                    Players.get(i).getTruck().crew.reduceCrew(numMembers);
                    Players.get(i).updateMoney(cosmicCredits);
                    Players.get(i).updatePosition(-days);
                    i = size; //al pari di un break
                }
                i++;
            }
        }
    }
}
