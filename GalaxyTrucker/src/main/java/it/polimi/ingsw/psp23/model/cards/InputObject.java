package it.polimi.ingsw.psp23.model.cards;

import java.util.ArrayList;
import java.util.List;

/*
 * Questa classe servirà a passare una serie di oggetti in input al metodo play senza dover creare etichette diverse
 */
public class InputObject {

    private boolean decision;

    private boolean decisionCannon;

    private boolean decisionEngine;

    private int numBatteriesCannon;

    private int numBatteriesEngine;

    /*
     * Questa lista servirà in AbandonedShip per sapere le coordinate della cabina da cui togliere il/i membri e
     * quanti se ne vogliono togliere(la lista contiene array di interi perchè ogni array conterrà due integer per le
     * coordinate ed un integer per il numero di membri da togliere)
    */
    private List<Integer[]> lista = new ArrayList<Integer[]>();

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

    public void addLista(int x, int y, int quantity) {
        lista.add(new Integer[]{x, y, quantity});
    }

    public List<Integer[]> getLista() {
        return new ArrayList<>(lista);
    }
}
