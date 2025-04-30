package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.exceptions.*;
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

    public void setPlanetOccupation(int i) throws CardException{
        // 1) Controllo che l’indice sia valido
        if (i < 0 || i >= planetsOccupied.size()) {
            throw new CardException("Planet index out of bounds: " + i);
        }
        // 2) Se non è occupato, occupalo
        if (planetsOccupied.get(i) == null) {
            planetsOccupied.set(i, Game.getInstance().getCurrentPlayer().getNickname());
        }
        // 3) Altrimenti lancia l’eccezione generica
        else {
            throw new CardException("Planet " + (i + 1)
                    + " is already occupied by " + planetsOccupied.get(i));
        }
    }

    public void loadGoods(int i, int j) throws CardException{
        int y = 0;
        int p = planetsOccupied.indexOf(Game.getInstance().getCurrentPlayer().getNickname());
        List<Item> items = planetGoods.get(p);
        if(y < items.size()){
            Board board = Game.getInstance().getCurrentPlayer().getTruck();
            Component[][] ship = board.getShip();
            Component tile = ship[i][j];
            switch (tile) {
                case Container container -> {
                    int index = board.getContainers().indexOf(container);
                    if (index == -1) {
                        throw new CardException("Container not found in 'containers' list: error in loadGoods of Board");
                    }

                        try {
                            // loadItem controlla anche se l'item può essere caricato in quello specifico container
                            board.getContainers().get(index).loadItem(items.get(index));

                        } catch (CardException c) {
                            // Rilancio una ContainerException con maggior contesto, da gestire poi nel Controller
                            throw new CardException("Item at index cannot be loaded in container at [" + i + "][" + j + "]: " + c.getMessage());
                        }

                }
                default -> throw new CardException("Component at ["+i+"]["+j+"] is not a container");
            }
        }
        else{
            throw new CardException("Merci esaurite");
        }
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








