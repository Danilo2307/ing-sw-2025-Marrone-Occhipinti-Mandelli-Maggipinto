package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Planets extends Card {
    private final int daysLost;
    private final List<List<Item>> planetGoods;
    private boolean[] planetsOccupied;

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

    @Override
    public Object call(VisitorParametrico visitorParametrico, int index){

        if(index < 0 || index >= planetGoods.size()){
            throw new IllegalArgumentException("Planets index out of bounds in method call of Planets card");
        }

        return visitorParametrico.visitForPlanets(this, index);

    }

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForPlanets(this);
    }

}



