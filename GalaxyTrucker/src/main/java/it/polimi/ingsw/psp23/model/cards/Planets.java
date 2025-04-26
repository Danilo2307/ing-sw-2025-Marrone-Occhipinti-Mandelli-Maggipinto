package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.exceptions.PlanetAlreadyTakenException;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

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

    public void initPlay(){
        Game.getInstance().setGameStatus(GameStatus.RunningPlanets);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus()));
    }

    public void play(InputObject inputObject){
        Player player = Game.getInstance().getCurrentPlayer();
        if(inputObject.getNumPlanet() == 1){
            if(planetsOccupied[0]){
                throw new PlanetAlreadyTakenException("Planet already occupied");
            }else{
                //il caricamento avviene mettendo tante posizioni di container quante sono le merci da caricare, anche se più merci vanno messe nello stesso container
                planetsOccupied[0] = true;

                for(int i = 0; i < planetGoods.get(0).size(); i++) {

                    int coordX = inputObject.getContainers().get(i)[0];

                    int coordY = inputObject.getContainers().get(i)[1];

                    Component analizedComponent = player.getTruck().getTile(coordX, coordY);

                    Container container = player.getTruck().getContainers().get(player.getTruck().getContainers().indexOf(analizedComponent));

                    container.loadItem(planetGoods.get(0).get(i)); //lancia un eccezione in caso di carico non valido per quel container o di container pieno


                }
            }
        }else if(inputObject.getNumPlanet() == 2){
            if(planetsOccupied[1]){
                throw new PlanetAlreadyTakenException("Planet already occupied");
            }else{
                //il caricamento avviene mettendo tante posizioni di container quante sono le merci da caricare, anche se più merci vanno messe nello stesso container
                planetsOccupied[1] = true;

                for(int i = 0; i < planetGoods.get(1).size(); i++) {

                    int coordX = inputObject.getContainers().get(i)[0];

                    int coordY = inputObject.getContainers().get(i)[1];

                    Component analizedComponent = player.getTruck().getTile(coordX, coordY);

                    Container container = player.getTruck().getContainers().get(player.getTruck().getContainers().indexOf(analizedComponent));

                    container.loadItem(planetGoods.get(1).get(i)); //lancia un eccezione in caso di carico non valido per quel container o di container pieno


                }
        }
    }else if(inputObject.getNumPlanet() == 3) {
            if (planetGoods.size() < 3)
                throw new RuntimeException("Planet number not valid");
            else {
                if (planetsOccupied[2]) {
                    throw new PlanetAlreadyTakenException("Planet already occupied");
                } else {
                    //il caricamento avviene mettendo tante posizioni di container quante sono le merci da caricare, anche se più merci vanno messe nello stesso container
                    planetsOccupied[2] = true;

                    for (int i = 0; i < planetGoods.get(2).size(); i++) {

                        int coordX = inputObject.getContainers().get(i)[0];

                        int coordY = inputObject.getContainers().get(i)[1];

                        Component analizedComponent = player.getTruck().getTile(coordX, coordY);

                        Container container = player.getTruck().getContainers().get(player.getTruck().getContainers().indexOf(analizedComponent));

                        container.loadItem(planetGoods.get(2).get(i)); //lancia un eccezione in caso di carico non valido per quel container o di container pieno

                    }
                }
            }
        }else if(inputObject.getNumPlanet() == 4) {
            if (planetGoods.size() < 4)
                throw new RuntimeException("Planet number not valid");
            else {
                if (planetsOccupied[3]) {
                    throw new PlanetAlreadyTakenException("Planet already occupied");
                } else {
                    //il caricamento avviene mettendo tante posizioni di container quante sono le merci da caricare, anche se più merci vanno messe nello stesso container
                    planetsOccupied[3] = true;

                    for (int i = 0; i < planetGoods.get(3).size(); i++) {

                        int coordX = inputObject.getContainers().get(i)[0];

                        int coordY = inputObject.getContainers().get(i)[1];

                        Component analizedComponent = player.getTruck().getTile(coordX, coordY);

                        Container container = player.getTruck().getContainers().get(player.getTruck().getContainers().indexOf(analizedComponent));

                        container.loadItem(planetGoods.get(3).get(i)); //lancia un eccezione in caso di carico non valido per quel container o di container pieno

                    }
                }
            }
        }else{
            throw new RuntimeException("Planet number not valid");
        }
    }
}








