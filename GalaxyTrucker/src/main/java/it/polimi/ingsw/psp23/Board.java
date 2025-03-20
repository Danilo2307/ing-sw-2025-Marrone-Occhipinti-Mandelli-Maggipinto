package it.polimi.ingsw.psp23;
import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.components.*;

import java.util.ArrayList;
import java.util.List;


public class Board {
    private Component[][] ship;
    private ArrayList<Component> garbage;
    private ArrayList<BatteryHub> batteryHubs;
    private ArrayList<AlienAddOns> alienAddOns;
    private ArrayList<StructuralComponent> structuralComponents;
    private ArrayList<Container> containers;
    private ArrayList<HousingUnit> housingUnits;


    public Board() {
        // per ora istanzio la ship come una 5 x 5, ma la dimensione effettiva sarà da definire
        ship = new Component[5][7];
        garbage = new ArrayList<Component>();
        batteryHubs = new ArrayList<>();
        alienAddOns = new ArrayList<>();
        structuralComponents = new ArrayList<>();
        containers = new ArrayList<>();
        housingUnits = new ArrayList<>();
    }

    public Board(Board other) {
        // costruttore di copia
        this.ship = other.ship;
        this.garbage.addAll(other.garbage);
        this.batteryHubs.addAll(other.batteryHubs);
        this.alienAddOns.addAll(other.alienAddOns);\
        structuralComponents.addAll(other.structuralComponents);
        containers.addAll(other.containers);
        housingUnits.addAll(other.housingUnits);
        /* TODO: chiarire se vogliamo usare il costruttore per creare una lista indipendente o una
                 lista dipendente da other, perchè se la volessi fare indipendente dovrei cambiare
                 metodo di istanziamento di tutte le liste usando i vari costruttori di copia
                 di tutti i vari tipi di components, perchè così sto passando ad ogni elemento
                 della lista i riferimenti agli elementi dell'altra lista. Questo controllo di
                 gestione va fatto anche per "ship"*/
    }


    public boolean check() {
    }

    public Component[][] getShip() {
    }

    public boolean isValid(int i, int j) {
    }

    public void delete(int i, int j) {
        if(!isValid(i, j) || ship[i][j] == null) {
            throw new IllegalArgumentException("There isn't any component in this slot or your indexes are invalid: exception in delete(i,j) of Board");
        }
        else {
            // È importante che i "type" in component siano scritti in PascalCase!!!
            if(ship[i][j].getType().equals("AlienAddOns")) {
                /* Nelle ArrayList il metodo remove ha due implementazioni, una che riceve in ingresso
                   l'indice dell'elemento da rimuovere ed una che riceve un oggetto e riceverà la prima
                   occorrenza dell'oggetto da rimuovere. Io sto usando la seconda.*/
                /*
                Inoltre, metto la rimozione in un if nel caso in cui non trovasse l'elemento,
                il che sarebbe un problema bello peso!!!
                 */
                if (!alienAddOns.remove(ship[i][j])) {
                    System.out.println("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di alienAddOns");
                }
            }
            else if(ship[i][j].getType().equals("BatteryHub")) {
                if(!batteryHubs.remove(ship[i][j])){
                    System.out.println("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di batteryHubs");
                }
            }
            else if(ship[i][j].getType().equals("Container")) {

                if(!containers.remove(ship[i][j])){
                    System.out.println("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di containers");
                }
            }
            else if(ship[i][j].getType().equals("HousingUnit")) {
                if(!housingUnits.remove(ship[i][j])){
                    System.out.println("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di housingUnits");
                }
            }
            else if(ship[i][j].getType().equals("StructuralComponent")) {
                if(!structuralComponents.remove(ship[i][j])){
                    System.out.println("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di structuralComponents");
                }
            }

            /* Ho aggiunto questa condizione nel caso dovessimo inserire un type in maniera errata
               di modo che sappiamo dove controllare */
            else{
                throw new IllegalArgumentException("There isn't any component like this in the lists: exception in delete(i,j) of Board");
            }
            ship[i][j] = null;
        }
    }

    public boolean isFree(int i, int j) {
    }

    public void reduceBatteries() {
    }

    public void reduceCrew(int numMembers) {
    }

    public List<Component> searchComponent(Component c) {
    }

    public void addComponent(Component c, int i, int j) {
    }

    public void releaseComponent(int i, int j) {
    }

    public void calculateExposedConnectors() {
    }

    public int getExposedConnectors() {
        return exposedConnectors;
    }

    public double getCannonStrength() {
        return cannonStrength;
    }

    public int getEngineStrength() {
        return engineStrength;
    }

    public Component[][] getShip() {
    }

    public int getCrew() {
        return crew;
    }

    public void pickMostImportantGoods(int numGoodsStolen) {
    }

    public void handleCannonShot(CannonShot cannonShot, int impactLine) {
        // nel caso in cui sia una cannonata grossa non possiamo fare niente perchè distrugge
        /*
        in questo metodo dobbiamo stare attenti al fatto che le colonne nel gioco del livello 2
        la plancia della nave ha 5 righe(numerate dal 5 al 9) e 7 colonne(numerate da 4 a 10),
        quindi in questo metodo gestisco prima la conversione di impactLine adattandola agli
        indici della nostra matrice
         */
        if(cannonShot.isBig()){
            while(!isValid() && )
        }
    }

    public void handleMeteor(Meteor meteor, int impactLine) {
    }

    public void loadGoods(List<Item> items) {
    }

    public void checkHousingUnits() {
    }
}
