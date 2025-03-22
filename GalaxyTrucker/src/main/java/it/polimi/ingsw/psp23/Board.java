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

        //prima di tutto converto la impactLine per far sì che rientri nei limiti della mia matrice
        int realImpactLine;
        if(meteor.getDirection() == Direction.UP || meteor.getDirection() == Direction.DOWN) {
            realImpactLine = impactLine-4;
        }
        else{
            realImpactLine = impactLine-5;
        }

        if(!meteor.isBig()){
            boolean isCovered = false;

            for(StructuralComponent s: structuralComponents){
                // devo mettere una serie di if per associare la direzione al lato dei componenti
                if(meteor.getDirection() == Direction.UP){
                    if(s.getUp() == Side.SHIELD || s.getUp() == Side.SHIELD_SINGLE_CONNECTOR || s.getUp() == Side.SHIELD_DOUBLE_CONNECTOR ){
                        isCovered = true;
                        break;
                    }
                }
                else if(meteor.getDirection() == Direction.DOWN){
                    if(s.getDown() == Side.SHIELD || s.getDown() == Side.SHIELD_SINGLE_CONNECTOR || s.getDown() == Side.SHIELD_DOUBLE_CONNECTOR ){
                        isCovered = true;
                        break;
                    }
                }
                else if(meteor.getDirection() == Direction.RIGHT){
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

            if(!isCovered){
                if(meteor.getDirection() == Direction.UP){
                    for(int i = 0; i < ship.length; i++) {
                        if(isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            if(ship[i][realImpactLine].getUp() != Side.EMPTY) {
                                delete(i, realImpactLine);
                            }
                            break;
                        }
                    }
                }
                else if(meteor.getDirection() == Direction.DOWN){
                    for(int i = ship.length-1; i >= 0; i--) {
                        if(isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            if(ship[i][realImpactLine].getDown() != Side.EMPTY) {
                                delete(i, realImpactLine);
                            }
                            break;
                        }
                    }
                }
                else if(meteor.getDirection() == Direction.RIGHT){
                    for(int j = ship[realImpactLine].length-1; j >= 0; j--) {
                        if(isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                            if(ship[realImpactLine][j].getRight() != Side.EMPTY) {
                                delete(realImpactLine, j);
                            }
                            break;
                        }
                    }
                }
                else{
                    for(int j = 0; j < ship[realImpactLine].length; j++) {
                        if(isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                            if(ship[realImpactLine][j].getLeft() != Side.EMPTY) {
                                delete(realImpactLine, j);
                            }
                            break;
                        }
                    }
                }
            }

        }
        else{
            /* Adesso, per la direzione up e down, scorro la lista di cannoni singoli o doppi e vedo se ce n'è almeno
               uno che abbia la colonna corrispondente con la colonna di arrivo del meteorite e che sia rivolto
               verso il lato corretto. Per le direzioni laterali devo invece controllare che ce ne siano anche due
               adiacenti perchè in caso potrebbero farlo esplodere.
               Non mi preoccupo di controllare che il cannone trovato sia il primo della riga/colonna perchè
               do per scontato che in questa fase del gioco la nave sia fatta bene e quindi con nessun componente
               davanti alla bocca dei cannoni in quella direzione
             */
            boolean isDestroyed = false; // questa variabile è praticamente la stessa di isCovered nel caso di prima
                                         // non ne uso una unica perchè mettendo nomi adatti il codice è più leggibile
            // distinguo i casi delle varie direzioni
            if(meteor.getDirection() == Direction.UP){
                for(StructuralComponent s: structuralComponents){
                    if(s.getY() == realImpactLine && (s.getUp() == Side.SINGLE_GUN || s.getUp() == Side.DOUBLE_GUN)){
                        isDestroyed = true;
                        break;
                    }
                }
            }
            else if(meteor.getDirection() == Direction.DOWN){
                for(StructuralComponent s: structuralComponents){
                    if(s.getY() == realImpactLine && (s.getDown() == Side.SINGLE_GUN || s.getDown() == Side.DOUBLE_GUN)){
                        isDestroyed = true;
                        break;
                    }
                }
            }
            else if(meteor.getDirection() == Direction.RIGHT){
                for(StructuralComponent s: structuralComponents){
                    //devo inserire il controllo che sia nelle celle adiacenti alla linea di arrivo essendo che arriva dal lato
                    if((s.getY() == realImpactLine || s.getY() == realImpactLine + 1 || s.getY() == realImpactLine - 1) && (s.getRight() == Side.SINGLE_GUN || s.getRight() == Side.DOUBLE_GUN)){
                        isDestroyed = true;
                        break;
                    }
                }
            }
            else{
                for(StructuralComponent s: structuralComponents){
                    //devo inserire il controllo che sia nelle celle adiacenti alla linea di arrivo essendo che arriva dal lato
                    if((s.getY() == realImpactLine || s.getY() == realImpactLine + 1 || s.getY() == realImpactLine - 1) && (s.getLeft() == Side.SINGLE_GUN || s.getLeft() == Side.DOUBLE_GUN)){
                        isDestroyed = true;
                        break;
                    }
                }
            }
            if(!isDestroyed){
                if(meteor.getDirection() == Direction.UP){
                    for(int i = 0; i < ship.length; i++) {
                        if(isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            delete(i, realImpactLine);
                            break;
                        }
                    }
                }
                else if(meteor.getDirection() == Direction.DOWN){
                    for(int i = ship.length-1; i >= 0; i--) {
                        if(isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            delete(i, realImpactLine);
                            break;
                        }
                    }
                }
                else if(meteor.getDirection() == Direction.RIGHT){
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

    /*
    Il ragionamento di loadGoods sarà: se ship[i][j] è un container prova ad aggiungere la lista di items fino
    a quando riesci; se l'aggiunta dovesse fallire o per incompatibilità di colori o per
    dimensione massima raggiunta lancia un'eccezione, quindi andrebbe gestita un'eventuale ritentativo
    di aggiunta degli elementi in un altro container
     */
    public void loadGoods(List<Item> items, int i, int j) {
        if(!isValid(i, j)|| ship[i][j] == null ||!ship[i][j].getType().equals("Container")) {
            throw new IllegalArgumentException("This is not a container: error in loadGoods of Board");
        }
        else{
            int indice = containers.indexOf(ship[i][j]);
            if(indice == -1){
                throw new IllegalArgumentException("Container not found in 'containers' list: error in loadGoods of Board");
            }
            int scorr = 0;
            while(scorr < items.size()) {
                if(containers.get(indice).loadItem(items.get(scorr))){
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

    public void reduceBatteries(int num, int i, int j) {
    /* Dopo aver interagito con la UI, il player decide quante batterie vuole usare e per ogni posizione
       controller chiama reduceBatteries specificandone la quantità e coordinate */
        if ((!isValid(i, j)) || (ship[i][j] == null) || (!"BatteryHub".equals(ship[i][j].getType()))) {
            throw new IllegalArgumentException("This is not a battery hub: error in reduceBatteries of Board");
        } else {
            int indice = batteryHubs.indexOf(ship[i][j]);
            if (indice == -1) {
                throw new IllegalArgumentException("BatteryHub not found in 'batteryHubs' list: error in reduceBatteries of Board");
            } else {
                int numbatteries = batteryHubs.get(indice).getNumBatteries();
                if (num <= 0 || num > numbatteries) {
                    System.out.println("Error: numBatteries not valid");
                    // TODO: la view richiede un nuovo numero
                } else {
                    int newbatteries = numbatteries - num;
                    batteryHubs.get(indice).setNumBatteries(newbatteries);
                }
            }
        }
    }


}
