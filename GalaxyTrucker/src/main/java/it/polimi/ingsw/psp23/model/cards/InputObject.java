package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.model.components.Shield;

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

    private int numHousingUnits;

    private int numPlanet;

    /*
     * Questa lista servirà in AbandonedShip per sapere le coordinate della cabina da cui togliere il/i membri e
     * quanti se ne vogliono togliere(la lista contiene array di interi perchè ogni array conterrà due integer per le
     * coordinate ed un integer per il numero di membri da togliere)
    */
    private List<Integer[]> housingUnit = new ArrayList<Integer[]>();

    private List<Integer[]> shields = new ArrayList<Integer[]>();

    private List<Integer[]> cannons = new ArrayList<Integer[]>();

    private List<Integer[]> engines = new ArrayList<Integer[]>();

    private List<Integer[]> housingUnits = new ArrayList<Integer[]>();

    private List<Integer[]> containers = new ArrayList<Integer[]>();

    private List<Item> toRemove = new ArrayList<Item>(); //lista di item da rimuovere combinati con le posizioni del vettore containers

    private List<Shield> activatedShields = new ArrayList<Shield>(); //posizione degli scudi attivati, null se non attivati



    public List<Shield> getActivatedShields(){
        return activatedShields;
    }

    public int getNumPlanet(){
        return numPlanet;
    }

    public void setNumPlanet(int numPlanet){
        this.numPlanet = numPlanet;
    }

    public int getNumHousingUnits(){
        return numHousingUnits;
    }

    public List<Item> getToRemove(){
        return toRemove;
    }

    public List<Integer[]> getHousingUnits(){
        return housingUnits;
    }

    public List<Integer[]> getContainers(){
        return containers;
    }

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

    public void addShield(int x, int y) {shields.add(new Integer[]{x, y});
    }

    public List<Integer[]> getShields() {
        return new ArrayList<>(shields);
    }

    public void addCannon(int x, int y) {cannons.add(new Integer[]{x, y});
    }

    public List<Integer[]> getCannons() {
        return new ArrayList<>(cannons);
    }

    public void addEngine(int x, int y) {engines.add(new Integer[]{x, y});
    }

    public List<Integer[]> getEngines() {
        return new ArrayList<>(engines);
    }


}
