package it.polimi.ingsw.psp23.model.cards;
public class AbandonedShip extends Card {
    //Danilo

    private final int days;
    private final int cosmicCredits;
    private final int numMembers;

    public AbandonedShip(int level,int days, int cosmicCredits, int numMembers) {
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

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForAbandonedShip(this);
    }
}