package it.polimi.ingsw.psp23;

public class AbandonedShip {
    //Danilo

    int days;
    int cosmicCredits;
    int numMembers;

    public AbandonedShip(boolean isSold, int days, int cosmicCredits, int numMembers) {
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

    public void play(List<Player> Players){
        boolean isSold = false;
        int size = Players.size();
        int i = 0;
        while(i < size){
            if(Players.get(i).getTruck().crew > numMembers && Players.get(i).isInGame) {
                isSold = true; // qui ricevo la risposta del player e la metto nella variabile isSold
                if (isSold) {
                    Players.get(i).getTruck().reduceHubCrew(numMembers); //serve a scegliere da quali hub togliere i membri
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
