package it.polimi.ingsw.psp23.model.cards;


public class Slavers extends Card {
    //Federico
    // final perchè non vengono più modificati dopo l'inizializzazione
    private final int cannonStrength;
    private final int membersStolen;
    private final int prize;
    private final int days;

    public Slavers(int level, int cannonStrength, int membersStolen, int prize, int days){
        super(level);
        this.cannonStrength = cannonStrength;
        this.membersStolen = membersStolen;
        this.prize = prize;
        this.days = days;
    }

    public int getCannonStrength() {
        return cannonStrength;
    }

    public int getMembersStolen() {
        return membersStolen;
    }

    public int getPrize() {
        return prize;
    }

    public int getDays() {
        return days;
    }
}