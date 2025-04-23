package it.polimi.ingsw.psp23.model.cards;

/*
 * Questa classe servirà a passare una serie di oggetti in input al metodo play senza dover creare etichette diverse
 */
public class InputObject {
    private boolean decision;
    private int numBatteries;
    public void setDecision(boolean decision) {
        this.decision = decision;
    }
    public void setNumBatteries(int numBatteries) {
        this.numBatteries = numBatteries;
    }
    public int getNumBatteries() {
        return numBatteries;
    }
    public boolean getDecision() {
        return decision;
    }
}
