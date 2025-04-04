package it.polimi.ingsw.psp23;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import it.polimi.ingsw.psp23.model.enumeration.ComponentType;

import java.util.ArrayList;
import java.util.List;


public class Board {
    private final Component[][] ship;
    // per i rifiuti mi interessa solo il numero, non sapere il tipo degli oggetti scartati
    private int garbage;
    // creo liste dei sottotipi per poter chiamare i metodi specifici e facilitare la ricerca
    private final ArrayList<BatteryHub> batteryHubs;
    private final ArrayList<AlienAddOns> alienAddOns;
    private final ArrayList<Cannon> cannons;
    private final ArrayList<Engine> engines;
    private final ArrayList<Shield> shields;
    private final ArrayList<Container> containers;
    private final ArrayList<HousingUnit> housingUnits;
    private final ArrayList<StructuralComponent> structuralComponents;
    private final int ROWS = 5;
    private final int COLS = 7;


    public Board() {
        ship = new Component[ROWS][COLS];
        garbage = 0;
        batteryHubs = new ArrayList<>();
        alienAddOns = new ArrayList<>();
        cannons = new ArrayList<>();
        engines = new ArrayList<>();
        shields = new ArrayList<>();
        containers = new ArrayList<>();
        housingUnits = new ArrayList<>();
        structuralComponents = new ArrayList<>();
    }

    // controllo legalità della nave
    public boolean check() {
        //empty space va solo con empty space nei componenti
        // double connector va o con universal o con double
        // single va o con single o con universal

        //scorro tutti componenti della plancia
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if(!isFree(i, j)) {
                    if(isValid(i-1,j )&& !isFree(i-1,j) && (ship[i-1][j].getType() == (ComponentType.ENGINE))) {
                        // il componente i,j sta sotto al motore di posizione i-1,j
                        return false;
                    }

                    // valuto componente alla destra del corrente i,j
                    if(isValid(i,j+1 ) && !isFree(i,j+1)){
                        if(ship[i][j+1].getLeft() == Side.GUN) {
                            return false;  // gun rivolta verso il mio componente
                        }
                        // controllo connettori
                        if((ship[i][j].getRight() == Side.EMPTY || ship[i][j].getRight() == Side.SHIELD )&& !(ship[i][j+1].getLeft() == Side.EMPTY)) { // empty con qualcosa di non empty
                            return false;
                        }
                        if((ship[i][j].getRight() == Side.SINGLE_CONNECTOR || ship[i][j].getRight() == Side.SHIELD_SINGLE_CONNECTOR) && !(ship[i][j+1].getLeft() == Side.SINGLE_CONNECTOR || ship[i][j+1].getLeft() == Side.UNIVERSAL_CONNECTOR || ship[i][j+1].getLeft() == Side.SHIELD_SINGLE_CONNECTOR)) {
                            return false;
                        }
                        if((ship[i][j].getRight() == Side.DOUBLE_CONNECTOR || ship[i][j].getRight() == Side.SHIELD_DOUBLE_CONNECTOR) && !(ship[i][j+1].getLeft() == Side.DOUBLE_CONNECTOR || ship[i][j+1].getLeft() == Side.UNIVERSAL_CONNECTOR || ship[i][j+1].getLeft() == Side.SHIELD_DOUBLE_CONNECTOR)){
                            return false;
                        }
                        if((ship[i][j].getRight() == Side.UNIVERSAL_CONNECTOR) && (ship[i][j+1].getLeft() == Side.EMPTY)){
                            return false;
                        }
                        if(ship[i][j+1].getLeft() == Side.ENGINE){
                            return false;
                        }
                    }

                    // valuto componente alla sinistra del corrente i,j
                    if(isValid(i,j-1 ) && !isFree(i,j-1)){
                        if(ship[i][j-1].getRight() == Side.GUN) { // gun rivolta verso il mio componente
                            return false;
                        }
                        // controllo connettori
                        if((ship[i][j].getLeft() == Side.EMPTY || ship[i][j].getLeft() == Side.SHIELD )&& !(ship[i][j-1].getRight() == Side.EMPTY)) { // empty con qualcosa di non empty
                            return false;
                        }
                        if((ship[i][j].getLeft() == Side.SINGLE_CONNECTOR || ship[i][j].getLeft() == Side.SHIELD_SINGLE_CONNECTOR) && !(ship[i][j-1].getRight() == Side.SINGLE_CONNECTOR || ship[i][j-1].getRight() == Side.UNIVERSAL_CONNECTOR || ship[i][j-1].getRight() == Side.SHIELD_SINGLE_CONNECTOR)) {
                            return false;
                        }
                        if((ship[i][j].getLeft() == Side.DOUBLE_CONNECTOR || ship[i][j].getLeft() == Side.SHIELD_DOUBLE_CONNECTOR) && !(ship[i][j-1].getRight() == Side.DOUBLE_CONNECTOR || ship[i][j-1].getRight() == Side.UNIVERSAL_CONNECTOR || ship[i][j-1].getRight() == Side.SHIELD_DOUBLE_CONNECTOR)){
                            return false;
                        }
                        if((ship[i][j].getLeft() == Side.UNIVERSAL_CONNECTOR) && (ship[i][j-1].getRight() == Side.EMPTY)){
                            return false;
                        }
                        if(ship[i][j-1].getRight() == Side.ENGINE){
                            return false;
                        }
                    }

                    // valuto componente al di sotto del corrente i,j
                    if(isValid(i+1,j ) && !isFree(i+1,j)){ //componente di sotto
                        if(ship[i+1][j].getUp() == Side.GUN) { // gun rivolta verso il mio componente
                            return false;
                        }
                        // controllo connettori
                        if((ship[i][j].getDown() == Side.EMPTY || ship[i][j].getDown() == Side.SHIELD )&& !(ship[i+1][j].getUp() == Side.EMPTY)) { // empty con qualcosa di non empty
                            return false;
                        }
                        if((ship[i][j].getDown() == Side.SINGLE_CONNECTOR || ship[i][j].getDown() == Side.SHIELD_SINGLE_CONNECTOR) && !(ship[i+1][j].getUp() == Side.SINGLE_CONNECTOR || ship[i+1][j].getUp() == Side.UNIVERSAL_CONNECTOR || ship[i+1][j].getUp() == Side.SHIELD_SINGLE_CONNECTOR)) {
                            return false;
                        }
                        if((ship[i][j].getDown() == Side.DOUBLE_CONNECTOR || ship[i][j].getDown() == Side.SHIELD_DOUBLE_CONNECTOR) && !(ship[i+1][j].getUp() == Side.DOUBLE_CONNECTOR || ship[i+1][j].getUp() == Side.UNIVERSAL_CONNECTOR || ship[i+1][j].getUp() == Side.SHIELD_DOUBLE_CONNECTOR)){
                            return false;
                        }
                        if((ship[i][j].getDown() == Side.UNIVERSAL_CONNECTOR) && (ship[i+1][j].getUp() == Side.EMPTY)){
                            return false;
                        }
                        if(ship[i+1][j].getUp() == Side.ENGINE){
                            return false;
                        }
                    }

                    // valuto componente che sta sopra al corrente i,j
                    if(isValid(i+1,j ) && !isFree(i+1,j)){
                        if(ship[i-1][j].getDown() == Side.GUN) { // gun rivolta verso il mio componente
                            return false;
                        }
                        if((ship[i][j].getUp() == Side.EMPTY || ship[i][j].getUp() == Side.SHIELD )&& !(ship[i-1][j].getDown() == Side.EMPTY)) { // empty con qualcosa di non empty
                            return false;
                        }
                        if((ship[i][j].getUp() == Side.SINGLE_CONNECTOR || ship[i][j].getUp() == Side.SHIELD_SINGLE_CONNECTOR) && !(ship[i-1][j].getDown() == Side.SINGLE_CONNECTOR || ship[i-1][j].getDown() == Side.UNIVERSAL_CONNECTOR || ship[i-1][j].getDown() == Side.SHIELD_SINGLE_CONNECTOR)) {
                            return false;
                        }
                        if((ship[i][j].getUp() == Side.DOUBLE_CONNECTOR || ship[i][j].getUp() == Side.SHIELD_DOUBLE_CONNECTOR) && !(ship[i-1][j].getDown() == Side.DOUBLE_CONNECTOR || ship[i-1][j].getDown() == Side.UNIVERSAL_CONNECTOR || ship[i-1][j].getDown() == Side.SHIELD_DOUBLE_CONNECTOR)){
                            return false;
                        }
                        if((ship[i][j].getUp() == Side.UNIVERSAL_CONNECTOR) && (ship[i-1][j].getDown() == Side.EMPTY)){
                            return false;
                        }
                        if(ship[i-1][j].getDown() == Side.ENGINE){
                            return false;
                        }
                    }
                }
            }
        }
        // se non ho fatto return false prima significa che è legale
        return true;
    }

    // indica se nella posizione i,j può starci un componente oppure no: la plancia non ha una forma perfettamente matriciale
    public boolean isValid(int i, int j) {

        final boolean[][] validPositions = new boolean[ROWS][COLS];

        int[][] validCoords = {
                {0, 2}, {0, 4}, {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, {3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {4, 0}, {4, 1}, {4, 2}, {4, 4}, {4, 5}, {4, 6}
        };

        for (int[] coord : validCoords) {
            int x = coord[0];
            int y = coord[1];
            validPositions[x][y] = true;
        }

        if (i < 0 || i >= ROWS || j < 0 || j >= COLS)
            return false;
        else
            return validPositions[i][j];
    }

    // determina se ship[i][j] contiene un component oppure no
    public boolean isFree(int i, int j) {
        return ship[i][j] == null;
    }

    public void setTrue(boolean[][] m, int i, int j) {
        m[i][j] = true;
    }

    /* Il ragionamento di questo metodo è: partendo dal modulo centrale, controlla che il componente in posizione i e j
       che stiamo cercando non sia l'adiacente, se non è l'adiacente, fai una chiamata ricorsiva sui moduli
     */
    // modCentrX e modCentrY mi servono come parametri per creare la funzione ricorsiva, altrimenti non riuscirei
    // a confrontare mano a mano tutti i componenti con il loro adiacente (se mettessimo questo metodo in component
    // per sapere se è raggiungibile potremmo risparmiarci i parametri i e j, in modo da semplificare l'etichetta del
    // metodo ma a queste "sottigliezze" ci penseremo più avanti)

    public boolean isReachable(boolean[][] alreadyChecked, int modCentrX, int modCentrY, int i, int j) {
        // inizializzo scorrX e scorrY alle coordinate del modulo centrale del livello due
        int scorrX = modCentrX;
        int scorrY = modCentrY;
        boolean check1 = false, check2 = false, check3 = false, check4 = false;

        setTrue(alreadyChecked, scorrX, scorrY);

        if (i == scorrX && j == scorrY) {
            return true;
        }
        if (isValid(scorrX + 1, scorrY) && !alreadyChecked[scorrX + 1][scorrY]) {
            if (scorrX + 1 == i && scorrY == j) {
                return true;
            } else {
                // setTrue(alreadyChecked, scorrX + 1, scorrY);
                check1 = isReachable(alreadyChecked, scorrX + 1, scorrY, i, j);
            }
        }
        if (isValid(scorrX - 1, scorrY) && !alreadyChecked[scorrX - 1][scorrY]) {
            if (scorrX - 1 == i && scorrY == j) {
                return true;
            } else {
                // setTrue(alreadyChecked, scorrX - 1, scorrY);
                check2 = isReachable(alreadyChecked, scorrX - 1, scorrY, i, j);
            }
        }

        if (isValid(scorrX, scorrY + 1) && !alreadyChecked[scorrX][scorrY + 1]) {
            if (scorrX == i && scorrY + 1 == j) {
                return true;
            } else {
                // setTrue(alreadyChecked, scorrX, scorrY + 1);
                check3 = isReachable(alreadyChecked, scorrX, scorrY + 1, i, j);
            }
        }

        if (isValid(scorrX, scorrY - 1) && !alreadyChecked[scorrX][scorrY - 1]) {
            if (scorrX == i && scorrY - 1 == j) {
                return true;
            } else {
                // setTrue(alreadyChecked, scorrX, scorrY - 1);
                check4 = isReachable(alreadyChecked, scorrX, scorrY - 1, i, j);
            }
        }
        return (check1 || check2 || check3 || check4);

    }


    // elimina elemento in posizione i,j della ship e lo rimuove dalla rispettiva lista
    // funziona perchè ship[i][j] e l'elemento della rispettiva lista puntano allo stesso oggetto in memoria:
    // la prima lo "vede" come Component (sovraclasse), mentre la lista lo vede come oggetto della sottoclasse
    public void delete(int i, int j) {
        boolean[][] alreadyChecked = new boolean[ROWS][COLS]; // scritto in questo modo sto inizializzando una matrice di booleani
        // che JAVA INIZIALIZZERÀ A FALSE
        if (!isValid(i, j) || isFree(i, j)) {
            throw new IllegalArgumentException("There isn't any component in this slot or your indexes are invalid: exception in delete(i,j) of Board");
        } else {
            /* Nelle ArrayList il metodo remove ha due implementazioni: uso quella che riceve un oggetto e rimuove il primo elemento
               su cui equals restituisce true: non serve override perchè equals confronta i riferimenti (in questo caso sono gli stessi). Controllo che esista lo stesso oggetto e venga rimosso correttamente.  */
            if (ship[i][j].getType() == ComponentType.ALIENADDONS) {
                if (!alienAddOns.remove(ship[i][j])) {
                    throw new IllegalArgumentException("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di alienAddOns");
                }
            } else if (ship[i][j].getType() == ComponentType.BATTERYHUB) {
                if (!batteryHubs.remove(ship[i][j])) {
                    throw new IllegalArgumentException("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di batteryHubs");
                }
            } else if (ship[i][j].getType() == ComponentType.CONTAINER) {
                if (!containers.remove(ship[i][j])) {
                    throw new IllegalArgumentException("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di containers");
                }
            } else if (ship[i][j].getType() == ComponentType.HOUSINGUNIT) {
                if (!housingUnits.remove(ship[i][j])) {
                    throw new IllegalArgumentException("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di housingUnits");
                }
            } else if (ship[i][j].getType() == ComponentType.CANNON) {
                if (!cannons.remove(ship[i][j])) {
                    throw new IllegalArgumentException("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di cannons");
                }
            } else if (ship[i][j].getType() == ComponentType.ENGINE) {
                if (!engines.remove(ship[i][j])) {
                    throw new IllegalArgumentException("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di engines");
                }
            } else if (ship[i][j].getType() == ComponentType.SHIELD) {
                if (!shields.remove(ship[i][j])) {
                    throw new IllegalArgumentException("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di shields");
                }
            } else if (ship[i][j].getType() == ComponentType.STRUCTURAL_COMPONENT) {
                if (!structuralComponents.remove(ship[i][j])) {
                    throw new IllegalArgumentException("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di structuralComponents");
                }

            /* Ho aggiunto questa condizione nel caso dovessimo inserire un type in maniera errata
               di modo che sappiamo dove controllare */
                else {
                    throw new IllegalArgumentException("There isn't any component like this in the lists: exception in delete(i,j) of Board");
                }
            }
            ship[i][j] = null;

            // adesso elimino i pezzi che non sono più raggiungibili, ricominciando il ciclo ogni volta che ne trovo uno
            // perchè potrebbe essere importante per collegare altri componenti
            // TODO: manca da analizzare il caso in cui venga eliminata il modulo centrale
            for (int row = 0; row < ship.length; row++) {
                for (int col = 0; col < ship[row].length; col++) {
                    // analizzando la matrice, 2 e 3 sono le coordinate del centro
                    if (!isReachable(alreadyChecked, 2, 3, row, col)) {
                        delete(row, col);
                        row = 0;
                        col = 0;
                    }
                }
            }
        }
    }

    public void addComponent(Component c, int i, int j) {
        if (isValid(i,j) && isFree(i, j)) {
            ship[i][j] = c;
            c.setX(i);
            c.setY(j);
            c.placeOnTruck();
        } else
            throw new IllegalArgumentException("You can't add this component: invalid parameters i and j");

        // aggiungo nella rispettiva lista in base al tipo. I due oggetti (in ship e nella lista) avranno
        // lo stesso riferimento, ma verranno visti da "due punti di vista diversi"
        switch (c.getType()) {
            case ALIENADDONS -> alienAddOns.add((AlienAddOns) c);
            case CANNON -> cannons.add((Cannon) c);
            case BATTERYHUB -> batteryHubs.add((BatteryHub) c);
            case CONTAINER -> containers.add((Container) c);
            case HOUSINGUNIT -> housingUnits.add((HousingUnit) c);
            case ENGINE -> engines.add((Engine) c);
            case STRUCTURAL_COMPONENT -> structuralComponents.add((StructuralComponent) c);
            case SHIELD -> shields.add((Shield) c);
        }
    }

    // il programma decide quali merci rimuovere e da quali container, a parità di preziosità.
    public void pickMostImportantGoods(int numGoodsStolen) {
        int itemsToLose = numGoodsStolen;
        // array che stabilisce l'ordine di preziosità delle merci e quindi l'ordine di priorità nella rimozione
        Color[] ordineGoods = {Color.Red, Color.Yellow, Color.Green, Color.Blue};
        for (Color color : ordineGoods) {
            for (Container container : containers) {
                for (Item item : container.getItems()) {
                    if (item.getColor() == color) {
                        container.loseItem(item);
                        itemsToLose--;
                    }
                    if (itemsToLose == 0)
                        break;
                }
                if (itemsToLose == 0)
                    break;
            }
            if (itemsToLose == 0)
                break;
        }
        // TODO: if itemsToLose>0 devo rimuovere le batterie
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
        if (cannonShot.getDirection() == Direction.UP || cannonShot.getDirection() == Direction.DOWN) {
            realImpactLine = impactLine - 4;
        } else {
            realImpactLine = impactLine - 5;
        }

        //gestisco separatamente il caso in cui la cannonata sia grossa e quello in cui sia piccola
        if (cannonShot.isBig()) {
            // devo distinguere i casi in cui arrivi da dx, sx, su e giù perchè dovrò scorrere la matrice in modo diverso
            if (cannonShot.getDirection() == Direction.UP) {
                for (int i = 0; i < ship.length; i++) {
                    if (isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                        delete(i, realImpactLine);
                        break;
                    }
                }
            } else if (cannonShot.getDirection() == Direction.DOWN) {
                for (int i = ship.length - 1; i >= 0; i--) {
                    if (isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                        delete(i, realImpactLine);
                        break;
                    }
                }
            } else if (cannonShot.getDirection() == Direction.RIGHT) {
                for (int j = ship[realImpactLine].length - 1; j >= 0; j--) {
                    if (isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                        delete(realImpactLine, j);
                        break;
                    }
                }
            } else {
                for (int j = 0; j < ship[realImpactLine].length; j++) {
                    if (isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                        delete(realImpactLine, j);
                        break;
                    }
                }
            }
        } else {
            // controllo se c'è uno scudo che difende quel lato
            // creo una variabile booleana e la metto a true se trovo lo scudo
            boolean isCovered = false;
            for (Shield s: shields) {
                // devo mettere una serie di if per associare la direzione al lato dei componenti
                if (cannonShot.getDirection() == Direction.UP) {
                    if (s.getUp() == Side.SHIELD || s.getUp() == Side.SHIELD_SINGLE_CONNECTOR || s.getUp() == Side.SHIELD_DOUBLE_CONNECTOR) {
                        isCovered = true;
                        break;
                    }
                } else if (cannonShot.getDirection() == Direction.DOWN) {
                    if (s.getDown() == Side.SHIELD || s.getDown() == Side.SHIELD_SINGLE_CONNECTOR || s.getDown() == Side.SHIELD_DOUBLE_CONNECTOR) {
                        isCovered = true;
                        break;
                    }
                } else if (cannonShot.getDirection() == Direction.RIGHT) {
                    if (s.getRight() == Side.SHIELD || s.getRight() == Side.SHIELD_SINGLE_CONNECTOR || s.getRight() == Side.SHIELD_DOUBLE_CONNECTOR) {
                        isCovered = true;
                        break;
                    }
                } else {
                    if (s.getLeft() == Side.SHIELD || s.getLeft() == Side.SHIELD_SINGLE_CONNECTOR || s.getLeft() == Side.SHIELD_DOUBLE_CONNECTOR) {
                        isCovered = true;
                        break;
                    }
                }
            }
            // in base al valore isCovered capisco se sono coperto in quel lato, se non sono coperto distruggo
            if (!isCovered) {
                if (cannonShot.getDirection() == Direction.UP) {
                    for (int i = 0; i < ship.length; i++) {
                        if (isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            delete(i, realImpactLine);
                            break;   // appena trovo il 1^ componente da distruggere esco; ci penserà delete a controllare i pezzi eventualmente scollegati
                        }
                    }
                } else if (cannonShot.getDirection() == Direction.DOWN) {
                    for (int i = ship.length - 1; i >= 0; i--) {
                        if (isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            delete(i, realImpactLine);
                            break;
                        }
                    }
                } else if (cannonShot.getDirection() == Direction.RIGHT) {
                    for (int j = ship[realImpactLine].length - 1; j >= 0; j--) {
                        if (isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                            delete(realImpactLine, j);
                            break;
                        }
                    }
                } else {
                    for (int j = 0; j < ship[realImpactLine].length; j++) {
                        if (isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
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
        if (meteor.getDirection() == Direction.UP || meteor.getDirection() == Direction.DOWN) {
            realImpactLine = impactLine - 4;
        } else {
            realImpactLine = impactLine - 5;
        }

        if (!meteor.isBig()) {
            boolean isCovered = false;
            // se la meteora è piccola, vedo se c'è uno shield che mi può difendere
            for (Shield s : shields) {
                // devo mettere una serie di if per associare la direzione al lato dei componenti
                if (meteor.getDirection() == Direction.UP) {
                    if (s.getUp() == Side.SHIELD || s.getUp() == Side.SHIELD_SINGLE_CONNECTOR || s.getUp() == Side.SHIELD_DOUBLE_CONNECTOR) {
                        isCovered = true;
                        break;
                    }
                } else if (meteor.getDirection() == Direction.DOWN) {
                    if (s.getDown() == Side.SHIELD || s.getDown() == Side.SHIELD_SINGLE_CONNECTOR || s.getDown() == Side.SHIELD_DOUBLE_CONNECTOR) {
                        isCovered = true;
                        break;
                    }
                } else if (meteor.getDirection() == Direction.RIGHT) {
                    if (s.getRight() == Side.SHIELD || s.getRight() == Side.SHIELD_SINGLE_CONNECTOR || s.getRight() == Side.SHIELD_DOUBLE_CONNECTOR) {
                        isCovered = true;
                        break;
                    }
                } else {
                    if (s.getLeft() == Side.SHIELD || s.getLeft() == Side.SHIELD_SINGLE_CONNECTOR || s.getLeft() == Side.SHIELD_DOUBLE_CONNECTOR) {
                        isCovered = true;
                        break;
                    }
                }
            }

            if (!isCovered) {
                if (meteor.getDirection() == Direction.UP) {
                    for (int i = 0; i < ship.length; i++) {
                        if (isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            if (ship[i][realImpactLine].getUp() != Side.EMPTY) {
                                delete(i, realImpactLine);
                            }
                            break;
                        }
                    }
                } else if (meteor.getDirection() == Direction.DOWN) {
                    for (int i = ship.length - 1; i >= 0; i--) {
                        if (isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            if (ship[i][realImpactLine].getDown() != Side.EMPTY) {
                                delete(i, realImpactLine);
                            }
                            break;
                        }
                    }
                } else if (meteor.getDirection() == Direction.RIGHT) {
                    for (int j = ship[realImpactLine].length - 1; j >= 0; j--) {
                        if (isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                            if (ship[realImpactLine][j].getRight() != Side.EMPTY) {
                                delete(realImpactLine, j);
                            }
                            break;
                        }
                    }
                } else {
                    for (int j = 0; j < ship[realImpactLine].length; j++) {
                        if (isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                            if (ship[realImpactLine][j].getLeft() != Side.EMPTY) {
                                delete(realImpactLine, j);
                            }
                            break;
                        }
                    }
                }
            }

        } else {
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
            if (meteor.getDirection() == Direction.UP) {
                for (Cannon s : cannons) {
                    if (s.getY() == realImpactLine && (s.getUp() == Side.GUN)) {
                        isDestroyed = true;
                        break;
                    }
                }
            } else if (meteor.getDirection() == Direction.DOWN) {
                for (Cannon s : cannons) {
                    if (s.getY() == realImpactLine && (s.getDown() == Side.GUN)) {
                        isDestroyed = true;
                        break;
                    }
                }
            } else if (meteor.getDirection() == Direction.RIGHT) {
                for (Cannon s : cannons) {
                    //devo inserire il controllo che sia nelle celle adiacenti alla linea di arrivo essendo che arriva dal lato
                    if ((s.getY() == realImpactLine || s.getY() == realImpactLine + 1 || s.getY() == realImpactLine - 1) && (s.getRight() == Side.GUN)) {
                        isDestroyed = true;
                        break;
                    }
                }
            } else {
                for (Cannon s : cannons) {
                    //devo inserire il controllo che sia nelle celle adiacenti alla linea di arrivo essendo che arriva dal lato
                    if ((s.getY() == realImpactLine || s.getY() == realImpactLine + 1 || s.getY() == realImpactLine - 1) && (s.getLeft() == Side.GUN)) {
                        isDestroyed = true;
                        break;
                    }
                }
            }
            // se la meteor non è stata distrutta ho impatto: devo rimuovere
            if (!isDestroyed) {
                if (meteor.getDirection() == Direction.UP) {
                    for (int i = 0; i < ship.length; i++) {
                        if (isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            delete(i, realImpactLine);
                            break;
                        }
                    }
                } else if (meteor.getDirection() == Direction.DOWN) {
                    for (int i = ship.length - 1; i >= 0; i--) {
                        if (isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            delete(i, realImpactLine);
                            break;
                        }
                    }
                } else if (meteor.getDirection() == Direction.RIGHT) {
                    for (int j = ship[realImpactLine].length - 1; j >= 0; j--) {
                        if (isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                            delete(realImpactLine, j);
                            break;
                        }
                    }
                } else {
                    for (int j = 0; j < ship[realImpactLine].length; j++) {
                        if (isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
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
        if (!isValid(i, j) || ship[i][j] == null || ship[i][j].getType() != ComponentType.CONTAINER) {
            throw new IllegalArgumentException("This is not a container: error in loadGoods of Board");
        } else {
            int index = containers.indexOf(ship[i][j]);
            if (index == -1) {
                throw new IllegalArgumentException("Container not found in 'containers' list: error in loadGoods of Board");
            }
            int scorr = 0;
            while (scorr < items.size()) {
                if (containers.get(index).canLoadItem(items.get(scorr))) {
                    containers.get(index).loadItem(items.get(scorr));
                    scorr++;
                } else {
                    throw new IllegalArgumentException("You can't add all the items here: error in loadGoods of Board");
                }
            }
        }
    }

    public void checkHousingUnits() {
        /* per ogni housingunit faccio un ciclo che controlli eventuali addons adiacenti, per poi aggiungere
           il colore dell'alienaddon corrispondente; non ho messo un break nel for
           quando trovo un addon perchè potrei avere più addons e quindi avere a disposizione più colori*/
        for (HousingUnit h : housingUnits) {
            for (AlienAddOns a : alienAddOns) {
                if ((a.getX() == h.getX() + 1 && a.getY() == h.getY()) || (a.getX() == h.getX() - 1 && a.getY() == h.getY()) || (a.getY() == h.getY() + 1 && a.getX() == h.getX()) || (a.getY() == h.getY() - 1 && a.getX() == h.getX())) {
                    h.addConnectedAddon(a.getColor());
                }
            }
        }
    }

    public void reduceBatteries(int num, int i, int j) throws IllegalType {
        //TODO: pensare se il giocatore può essere stupido o meno
        if ((!isValid(i, j)) || isFree(i,j) || !ship[i][j].getType().equals(ComponentType.BATTERYHUB)) {
            throw new IllegalType();
        } else {
            int index = batteryHubs.indexOf(ship[i][j]);
            if (index == -1) {
                throw new IllegalArgumentException("BatteryHub not found in 'batteryHubs' list: error in reduceBatteries of Board");
            } else {
                batteryHubs.get(index).removeBatteries(num);
            }
        }
    }

    public void reduceCrew(int num, int i, int j) throws IllegalType {
        if ((!isValid(i, j)) || isFree(i,j) || !ship[i][j].getType().equals(ComponentType.HOUSINGUNIT)) {
            throw new IllegalType();
        } else {
            int index = housingUnits.indexOf(ship[i][j]);
            if (index == -1) {
                throw new IllegalArgumentException("HousingUnit not found in 'housingUnit' list: error in reduceCrew of Board");
            } else {
                housingUnits.get(index).reduceOccupants(num);
            }
        }
        if (calculateCrew() == 0) {
            //TODO: gestire il caso in cui il giocatore non ha più equipaggio ed è costretto ad abbandonare la corsa
            System.out.println("Player must leave game!");
        }
    }

    public int calculateExposedConnectors() {
        /* Fa dei controlli lato per lato, analizzando esclusivamente i component che definiscono il bordo della nave,
         * e controlla se ci sono dei controllori esposti. In quel caso aggiorno il contatore*/
        int count = 0;
        int rows = ship.length;
        int cols = ship[0].length;
        //SIDE UP
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                if (isValid(i, j) && !isFree(i,j)) {
                    if (ship[i][j].getUp().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getUp().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getUp().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getUp().equals(Side.SHIELD_DOUBLE_CONNECTOR)) {
                        count++;
                    }
                    break;
                }
            }
        }
        //SIDE DOWN
        for (int j = 0; j < cols; j++) {
            for (int i = rows - 1; i >= 0; i--) {
                if (isValid(i, j) && !isFree(i,j)) {
                    if (ship[i][j].getDown().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getDown().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getDown().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getDown().equals(Side.SHIELD_DOUBLE_CONNECTOR)) {
                        count++;
                    }
                    break;
                }
            }
        }
        //SIDE LEFT
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isValid(i, j) && !isFree(i,j)) {
                    if (ship[i][j].getLeft().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getLeft().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getLeft().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getLeft().equals(Side.SHIELD_DOUBLE_CONNECTOR)) {
                        count++;
                    }
                    break;
                }
            }
        }
        //SIDE RIGHT
        for (int i = 0; i < rows; i++) {
            for (int j = cols - 1; j >= 0; j--) {
                if (isValid(i, j) && !isFree(i,j)) {
                    if (ship[i][j].getRight().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getRight().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getRight().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getRight().equals(Side.SHIELD_DOUBLE_CONNECTOR)) {
                        count++;
                    }
                    break;
                }
            }
        }
        return count;
    }

    public void activeCannon(int i, int j) throws IllegalType {
        if ((!isValid(i, j)) || isFree(i,j) || !ship[i][j].getType().equals(ComponentType.CANNON)) {
            throw new IllegalType();
        } else {
            int index = cannons.indexOf(ship[i][j]);
            if(!cannons.get(index).isDouble()){
                throw new IllegalArgumentException("This is not a double cannon!");
            }
            else{
                cannons.get(index).activeCannon();
            }
        }
    }

    public void activeEngine(int i, int j) throws IllegalType {
        if ((!isValid(i, j)) || isFree(i,j) || !ship[i][j].getType().equals(ComponentType.ENGINE)) {
            throw new IllegalType();
        } else {
            int index = engines.indexOf(ship[i][j]);
            if(!engines.get(index).isDouble()){
                throw new IllegalArgumentException("This is not a double engine!");
            }
            else{
                engines.get(index).activeEngine();
            }
        }
    }

    public double calculateCannonStrength() {

        // I cannoni singoli puntati in avanti contano +1, gli altri +½. Se spendi una batteria, i cannoni doppi puntati in avanti contano +2, gli altri +1.
        double strength = 0;
        for (Cannon gun : cannons) {
            if (gun.getUp().equals(Side.GUN)) {
                if(gun.isDouble()){
                    if(gun.checkIfIsActive()){
                        strength += 2;
                        gun.disactiveCannon();
                    }
                }
                else{
                    strength += 1;
                }
            } else {
                if(gun.isDouble()){
                    if(gun.checkIfIsActive()){
                        strength += 1;
                        gun.disactiveCannon();
                    }
                }
                else {
                    strength += 0.5;
                }
            }
        }
        // L’alieno viola conta +2, ma solo se la potenza di fuoco è già superiore a 0.
        if (strength > 0) {
            for (HousingUnit housing : housingUnits) {
                if (housing.getAlien().equals(Color.Purple)) {
                    strength += 2;
                    break;
                }
            }
        }
        return strength;
    }

    public int calculateEngineStrength() {
        /*  I motori singoli contano +1. I motori doppi contano +2 se spendi una batteria. L’alieno marrone
        conta +2, ma solo se la potenza motrice è già superiore a 0. */
        int strength = 0;
        for (Engine engine : engines) {
            if(engine.isDouble()){
                if(engine.checkIfIsActive()) {
                    strength += 2;
                    engine.disactiveEngine();
                }
            }
            else{
                strength += 1;
            }
        }
        for (HousingUnit housing : housingUnits) {
            if (housing.getAlien().equals(Color.Brown) && strength > 0) {
                strength += 2;
                break;
            }
        }
        return strength;
    }

    public int calculateBatteriesAvailable() {
        int numbatteries = 0;
        for (BatteryHub BatteryHub : batteryHubs) {
            numbatteries += BatteryHub.getNumBatteries();
        }
        return numbatteries;
    }

    public int calculateCrew() {
        int crew = 0;
        for (HousingUnit housing : housingUnits) {
            if (housing.getAlien() != null) {
                crew += 1;
            } else {
                crew += housing.getNumAstronaut();
            }
        }
        return crew;
    }

    public Component[][] getShip() {
        return ship;
    }

    public ArrayList<HousingUnit> getHousingUnitsList() {
        return housingUnits;
    }

    public boolean isWelded(){ //restituisce vero se il player ha almeno un pezzo saldato
        for(int i = 0; i < ROWS ; i++){
            for(int j = 0; j < COLS ; j++){
                if(i!= 2 && j!= 3) // non considero la cabina centrale
                    if(ship[i][j]!= null)
                        return true;
            }
        }
        return false;
    }
}
