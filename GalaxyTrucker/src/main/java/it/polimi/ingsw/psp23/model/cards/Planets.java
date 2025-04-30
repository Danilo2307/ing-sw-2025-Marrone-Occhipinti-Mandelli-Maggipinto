package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.exceptions.PlanetAlreadyTakenException;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import jdk.jfr.FlightRecorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Planets extends Card {
    private final int daysLost;
    private final List<List<Item>> planetGoods;
    private List<String> planetsOccupied;

    public Planets (int level, int daysLost, List<List<Item>> planetGoods) {
        super(level);
        this.daysLost = daysLost;
        this.planetGoods = planetGoods;
        this.planetsOccupied = new ArrayList<>(Collections.nCopies(planetGoods.size(), null));
    }

    public int getDaysLost() {
        return daysLost;
    }

    public List<List<Item>> getPlanetGoods() {
        return new ArrayList<>(planetGoods);
    }

    public String[] getPlanetsOccupied() {
        return planetsOccupied.toArray(new String[0]);
    }

    public void setPlanetOccupation(int i, String username) throws CardException{
        // 1) Controllo che l’indice sia valido
        if (i < 0 || i >= planetsOccupied.size()) {
            throw new CardException("Planet index out of bounds: " + i);
        }
        // 2) Se non è occupato, occupalo
        if (planetsOccupied.get(i) == null) {
            planetsOccupied.set(i, username);
        }
        // 3) Altrimenti lancia l’eccezione generica
        else {
            throw new CardException("Planet " + (i + 1)
                    + " is already occupied by " + planetsOccupied.get(i));
        }
    }

    @Override
    public Object call(VisitorParametrico visitorParametrico, int index, String player){

        if(index < 0 || index >= planetGoods.size()){
            throw new IllegalArgumentException("Planets index out of bounds in method call of Planets card");
        }

        return visitorParametrico.visitForPlanets(this, index, player);

    }

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForPlanets(this);
    }

    public void initPlay(){
        Game.getInstance().setGameStatus(GameStatus.INIT_PLANETS);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus(), daysLost, planetGoods));
    }

    public void play(){
        endPlay();
    }

    public void endPlay(){
    }

}








