package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Planets extends Card {
    private final int daysLost;
    private final List<List<Item>> planetGoods;
    private boolean[] planetsOccupied;
    private int planetsOccupiedIndex = 0; // ha una funzione analoga a quella di index presente
                                          // in CombatZone

    public Planets (int level, int daysLost, List<List<Item>> planetGoods) {
        super(level);
        this.daysLost = daysLost;
        this.planetGoods = planetGoods;
        planetsOccupied = new boolean[planetGoods.size()];
        for(int i = 0; i < planetGoods.size(); i++){
            planetsOccupied[i] = false;
        }
    }

    public int getDaysLost() {
        return daysLost;
    }

    public List<List<Item>> getPlanetGoods() {
        return new ArrayList<>(planetGoods);
    }

    public boolean[] getPlanetsOccupied() {
        return Arrays.copyOf(planetsOccupied, planetsOccupied.length);
    }

    public void setPlanetOccupation(int i){
        planetsOccupied[i] = true;
    }

    public void setPlanetsOccupiedIndex(int indice) {
        if(indice >= 0 && indice < planetGoods.size()){
            planetsOccupiedIndex = indice;
        }
        else{
            throw new IllegalArgumentException("Planets index out of bounds in Planets card");
        }
    }

    @Override
    public Object call(VisitorParametrico visitorParametrico){
        return visitorParametrico.visitForPlanets(this, planetsOccupiedIndex);
    }

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForPlanets(this);
    }

}



