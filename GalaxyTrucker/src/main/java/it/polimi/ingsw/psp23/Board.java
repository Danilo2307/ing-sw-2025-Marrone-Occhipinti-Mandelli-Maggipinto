package it.polimi.ingsw.psp23;
import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.Side;

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
            else if(ship[i][j].getType().equals("Gun") || ship[i][j].getType().equals("DoubleGun") || ship[i][j].getType().equals("Engine") || ship[i][j].getType().equals("DoubleEngine") || ship[i][j].getType().equals("StructuralModules") || ship[i][j].getType().equals("Shield")) {
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
        // TODO: manca il controllo se gli scudi sono attivati con le batterie o meno

        // nel caso in cui sia una cannonata grossa non possiamo fare niente perchè distrugge a prescindere
        /*
        in questo metodo dobbiamo stare attenti al fatto che le colonne nel gioco del livello 2
        la plancia della nave ha 5 righe(numerate dal 5 al 9) e 7 colonne(numerate da 4 a 10),
        quindi in questo metodo gestisco prima la conversione di impactLine adattandola agli
        indici della nostra matrice
         */
        // effettuo prima questa conversione
        int realImpactLine;
        if(cannonShot.getDirection() == Direction.UP || cannonShot.getDirection() == Direction.DOWN) {
            realImpactLine = impactLine-4;
        }
        else{
            realImpactLine = impactLine-5;
        }

        //gestisco separatamente il caso in cui la cannonata sia grossa e quello in cui sia piccola
        if(cannonShot.isBig()){
            // devo distinguere i casi in cui arrivi da dx, sx, su e giù perchè dovrò scorrere la matrice in modo diverso
            if(cannonShot.getDirection() == Direction.UP){
                for(int i = 0; i < ship.length; i++) {
                    if(isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                        delete(i, realImpactLine);
                        break;
                    }
                }
            }
            else if(cannonShot.getDirection() == Direction.DOWN){
                for(int i = ship.length-1; i >= 0; i--) {
                    if(isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                        delete(i, realImpactLine);
                        break;
                    }
                }
            }
            else if(cannonShot.getDirection() == Direction.RIGHT){
                for(int j = ship[realImpactLine].length-1; j >= 0; j--) {
                    if(isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                        delete(realImpactLine, j);
                        break;
                    }
                }
            }
            else{
                for(int j = 0; j < ship[realImpactLine].length; j++) {
                    if(isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                        delete(realImpactLine, j);
                        break;
                    }
                }
            }
        }
        else{
            // controllo se c'è uno scudo che difende quel lato
            // creo una variabile booleana e la metto a true se trovo lo scudo
            boolean isCovered = false;
            for(StructuralComponent s: structuralComponents){
                // devo mettere una serie di if per associare la direzione al lato dei componenti
                if(cannonShot.getDirection() == Direction.UP){
                    if(s.getUp() == Side.SHIELD || s.getUp() == Side.SHIELD_SINGLE_CONNECTOR || s.getUp() == Side.SHIELD_DOUBLE_CONNECTOR ){
                        isCovered = true;
                        break;
                    }
                }
                else if(cannonShot.getDirection() == Direction.DOWN){
                    if(s.getDown() == Side.SHIELD || s.getDown() == Side.SHIELD_SINGLE_CONNECTOR || s.getDown() == Side.SHIELD_DOUBLE_CONNECTOR ){
                        isCovered = true;
                        break;
                    }
                }
                else if(cannonShot.getDirection() == Direction.RIGHT){
                    if(s.getRight() == Side.SHIELD || s.getRight() == Side.SHIELD_SINGLE_CONNECTOR || s.getRight() == Side.SHIELD_DOUBLE_CONNECTOR ){
                        isCovered = true;
                        break;
                    }
                }
                else{
                    if(s.getLeft() == Side.SHIELD || s.getLeft() == Side.SHIELD_SINGLE_CONNECTOR || s.getLeft() == Side.SHIELD_DOUBLE_CONNECTOR ){
                        isCovered = true;
                        break;
                    }
                }
            }
            // in base al valore isCovered capisco se sono coperto in quel lato, se non sono coperto distruggo
            if(!isCovered){
                if(cannonShot.getDirection() == Direction.UP){
                    for(int i = 0; i < ship.length; i++) {
                        if(isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            delete(i, realImpactLine);
                            break;
                        }
                    }
                }
                else if(cannonShot.getDirection() == Direction.DOWN){
                    for(int i = ship.length-1; i >= 0; i--) {
                        if(isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            delete(i, realImpactLine);
                            break;
                        }
                    }
                }
                else if(cannonShot.getDirection() == Direction.RIGHT){
                    for(int j = ship[realImpactLine].length-1; j >= 0; j--) {
                        if(isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                            delete(realImpactLine, j);
                            break;
                        }
                    }
                }
                else{
                    for(int j = 0; j < ship[realImpactLine].length; j++) {
                        if(isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                            delete(realImpactLine, j);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void handleMeteor(Meteor meteor, int impactLine) {

    }

    /*
    Il ragionamento di loadGoods sarà: se ship[i][j] è un container prova ad aggiungere la lista di items fino
    a quando riesci; se l'aggiunta dovesse fallire o per incompatibilità di colori o per
    dimensione massima raggiunta lancia un'eccezione
     */
    public void loadGoods(List<Item> items, int i, int j) {
        if(!ship[i][j].getType().equals("Container")){
            throw new IllegalArgumentException("This is not a container: error in loadGoods of Board");
        }
        else{
            int indice = containers.indexOf(ship[i][j]);
            int scorr = 0;
            while(scorr < items.size()) {
                if(containers.get(indice).loadItem(items.get(i))){
                    scorr++;
                }
                else{
                    throw new IllegalArgumentException("You can't add all the items here: error in loadGoods of Board");
                }
            }
        }
    }

    public void checkHousingUnits() {
        /* per ogni housingunit faccio un ciclo che controlli eventuali addons adiacenti, per poi aggiungere
           il colore dell'alienaddon corrispondente; non ho messo un break nel for
           quando trovo un addon perchè potrei avere più addons e quindi avere a disposizione più colori*/
        for(HousingUnit h : housingUnits) {
            for(AlienAddOns a : alienAddOns) {
                if((a.getX() == h.getX() + 1 && a.getY() == h.getY())|| (a.getX() == h.getX() - 1 && a.getY() == h.getY()) || (a.getY() == h.getY() + 1 && a.getX() == h.getX()) || (a.getY() == h.getY() - 1 && a.getX() == h.getX())) {
                    h.addConnectedAddon(a.getColor());
                }
            }
        }
    }
}
