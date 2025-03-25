package it.polimi.ingsw.psp23;
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
    private Component[][] ship;
    private ArrayList<Component> garbage;
    private ArrayList<BatteryHub> batteryHubs;
    private ArrayList<AlienAddOns> alienAddOns;
    private ArrayList<Component> structuralComponents;
    private ArrayList<Container> containers;
    private ArrayList<HousingUnit> housingUnits;
    private final int ROWS = 5;
    private final int COLS = 7;


    public Board() {
        // per ora istanzio la ship come una 5 x 5, ma la dimensione effettiva sarà da definire
        ship = new Component[ROWS][COLS];
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
        this.alienAddOns.addAll(other.alienAddOns);
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


    public boolean check() { //restituisce un boolean che indica se la nave è legale o meno
        for (int i = 0; i < ROWS; i++) { //scorro tutti componenti della plancia
            for (int j = 0; j < COLS; j++) {
                if(!isFree(i, j)) {
                    if(isValid(i-1,j ) && (ship[i-1][j].getType() == (ComponentType.ENGINE) || ship[i][j].getType() == (ComponentType.DOUBLEENGINE))) {

                    }
                }
            }
        }
    }

    //




    public boolean isValid(int i, int j) {
       /* final boolean[][] validPositions = new boolean[5][7];
        validPositions[0][2] = true;
        validPositions[0][4] = true;
        validPositions[1][1] = true;
        validPositions[1][2] = true;
        validPositions[1][3] = true;
        validPositions[1][4] = true;
        validPositions[1][5] = true;
        validPositions[2][0] = true;
        validPositions[2][1] = true;
        validPositions[2][2] = true;
        validPositions[2][3] = true;
        validPositions[2][4] = true;
        validPositions[2][5] = true;
        validPositions[2][6] = true;
        validPositions[3][0] = true;
        validPositions[3][1] = true;
        validPositions[3][2] = true;
        validPositions[3][3] = true;
        validPositions[3][4] = true;
        validPositions[3][5] = true;
        validPositions[3][6] = true;
        validPositions[4][0] = true;
        validPositions[4][1] = true;
        validPositions[4][2] = true;
        validPositions[4][4] = true;
        validPositions[4][5] = true;
        validPositions[4][6] = true;*/

        final boolean[][] validPositions = new boolean[ROWS][COLS];

        int[][] validCoords = {
                {0, 2}, {0, 4}, {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5},{2, 0},{2, 1},{2, 2},{2, 3},{2, 4},{2, 5},{2, 6},{3, 0},{3, 1},{3, 2},{3, 3},{3, 4},{3, 5},{3, 6},{4, 0},{4, 1},{4, 2},{4, 4},{4, 5},{4, 6}
        };

        for (int[] coord : validCoords) {
            int x = coord[0];
            int y = coord[1];
            validPositions[x][y] = true;
        }

        if (i < 0 || i >= ROWS || j < 0 || j >= COLS)
            return false;
        else
            return true;

    }

    public void setTrue(boolean[][] m, int i, int j) {
        m[i][j]=true;
    }

    /* Il ragionamento di questo metodo è: partendo dal modulo centrale, controlla che il componente in posizione i e j
       che stiamo cercando non sia l'adiacente, se non è l'adiacente, fai una chiamata ricorsiva sui moduli
     */
    // modCentrX e modCentrY mi servono come parametri per creare la funzione ricorsiva, altrimenti non riuscirei
    // s confrontare mano a mano tutti i componenti con il loro adiacente(se mettessimo questo metodo in component
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
        if (isValid(scorrX + 1, scorrY) && alreadyChecked[scorrX + 1][scorrY] == false) {
            if (scorrX + 1 == i && scorrY == j) {
                return true;
            } else {
                // setTrue(alreadyChecked, scorrX + 1, scorrY);
                check1 = isReachable(alreadyChecked, scorrX + 1, scorrY, i, j);
            }
        }
        if (isValid(scorrX - 1, scorrY) && alreadyChecked[scorrX-1][scorrY] == false) {
            if (scorrX - 1 == i && scorrY == j) {
                return true;
            }
            else {
                // setTrue(alreadyChecked, scorrX - 1, scorrY);
                check2 = isReachable(alreadyChecked,scorrX - 1, scorrY, i, j);
            }
        }

        if (isValid(scorrX, scorrY + 1) && alreadyChecked[scorrX][scorrY+1] == false) {
            if (scorrX == i && scorrY + 1 == j) {
                return true;
            }
            else {
                // setTrue(alreadyChecked, scorrX, scorrY + 1);
                check3 = isReachable(alreadyChecked,scorrX, scorrY + 1, i, j);
            }
        }

        if (isValid(scorrX, scorrY - 1) && alreadyChecked[scorrX][scorrY-1] == false) {
            if (scorrX == i && scorrY - 1 == j) {
                return true;
            }
            else {
                // setTrue(alreadyChecked, scorrX, scorrY - 1);
                check4 = isReachable(alreadyChecked,scorrX, scorrY - 1, i, j);
            }
        }


        return (check1 || check2 || check3 || check4);

    }

    public void delete(int i, int j) {
        boolean[][] alreadyChecked = new boolean[ROWS][COLS]; // scritto in questo modo sto inizializzando una matrice di booleani
                                                        // che JAVA INIZIALIZZERÀ A FALSE
        if(!isValid(i, j) || ship[i][j] == null) {
            throw new IllegalArgumentException("There isn't any component in this slot or your indexes are invalid: exception in delete(i,j) of Board");
        }
        else {
            // È importante che i "type" in component siano scritti in PascalCase!!!
            if(ship[i][j].getType() == ComponentType.ALIENADDONS) {
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
            else if(ship[i][j].getType() == ComponentType.BATTERYHUB) {
                if(!batteryHubs.remove(ship[i][j])){
                    System.out.println("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di batteryHubs");
                }
            }
            else if(ship[i][j].getType() == ComponentType.CONTAINER) {

                if(!containers.remove(ship[i][j])){
                    System.out.println("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di containers");
                }
            }
            else if(ship[i][j].getType() == ComponentType.HOUSINGUNIT) {
                if(!housingUnits.remove(ship[i][j])){
                    System.out.println("Non è stato trovato l'elemento per eliminarlo in 'delete' di Board, controlla la lista di housingUnits");
                }
            }
            else if(ship[i][j].getType() == ComponentType.CANNON || ship[i][j].getType() == ComponentType.DOUBLECANNON || ship[i][j].getType() == ComponentType.ENGINE || ship[i][j].getType() == ComponentType.DOUBLEENGINE || ship[i][j].getType() == ComponentType.TUBE || ship[i][j].getType() == ComponentType.SHIELD) {
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

        // adesso elimino i pezzi che non sono più raggiungibili, ricominciando il ciclo ogni volta che ne trovo uno
        // perchè potrebbe essere importante per collegare altri componenti
        // TODO: manca da analizzare il caso in cui venga eliminata il modulo centrale
        for(int row = 0; row < ship.length ; row++){
            for(int col = 0; col < ship[row].length ; col++){
                // analizzando la matrice, 2 e 3 sono le coordinate del centro
                if(!isReachable(alreadyChecked, 2, 3, row, col)){
                    delete(row, col);
                    row = 0;
                    col = 0;
                }
            }
        }
    }

    public boolean isFree(int i, int j) {
        if(ship[i][j] == null)
            return true;
        else
            return false;
    }


    public List<Component> searchComponent(Component c) {
    }

    public void addComponent(Component c, int i, int j) {
        if (i >= 0 && i < ROWS && j >= 0 && j < COLS && isFree(i, j)) {
            ship[i][j] = c;
            c.setX(i);
            c.setY(j);
            c.placeOnTruck();
        }else
            throw new IllegalArgumentException("Invalid parameters i and j");

        switch(c.getType()) {
            case ALIENADDONS -> alienAddOns.add((AlienAddOns) c);
            case CANNON -> structuralComponents.add((StructuralComponent) c);
            case BATTERYHUB -> batteryHubs.add((BatteryHub) c);
            case CONTAINER -> containers.add((Container) c);
            case HOUSINGUNIT -> housingUnits.add((HousingUnit) c);
            case DOUBLECANNON -> structuralComponents.add((StructuralComponent) c);
            case DOUBLEENGINE -> structuralComponents.add((StructuralComponent) c);
            case ENGINE -> structuralComponents.add((StructuralComponent) c);
            case TUBE -> structuralComponents.add((StructuralComponent) c);
        }

    }


    public void pickMostImportantGoods(int numGoodsStolen) {
        // creo una variabile che mano a mano decrementerò per sapere quante merci/batterie mancano da essere
        // rubati
        int rubati = numGoodsStolen;
        Color[] ordineGoods = {Color.Red, Color.Yellow, Color.Green, Color.Blue};
        while(rubati > 0) {
            /* Faccio il seguente ragionamento: itero sull'array di color ordinati in ordine di preziosità e,
               appena posso togierlo lo tolgo e arresto il ciclo, altrimenti tolgo una batteria.*/
            boolean removed = false;
            for(Color colore : ordineGoods) {
                // TODO: serve la scelta da parte dell'utente sul container da svuotare
            }
            rubati--;
        }
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
            for(Component s: structuralComponents){
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

            for(Component s: structuralComponents){
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
                for(Component s: structuralComponents){
                    if(s.getY() == realImpactLine && (s.getUp() == Side.SINGLE_GUN || s.getUp() == Side.DOUBLE_GUN)){
                        isDestroyed = true;
                        break;
                    }
                }
            }
            else if(meteor.getDirection() == Direction.DOWN){
                for(Component s: structuralComponents){
                    if(s.getY() == realImpactLine && (s.getDown() == Side.SINGLE_GUN || s.getDown() == Side.DOUBLE_GUN)){
                        isDestroyed = true;
                        break;
                    }
                }
            }
            else if(meteor.getDirection() == Direction.RIGHT){
                for(Component s: structuralComponents){
                    //devo inserire il controllo che sia nelle celle adiacenti alla linea di arrivo essendo che arriva dal lato
                    if((s.getY() == realImpactLine || s.getY() == realImpactLine + 1 || s.getY() == realImpactLine - 1) && (s.getRight() == Side.SINGLE_GUN || s.getRight() == Side.DOUBLE_GUN)){
                        isDestroyed = true;
                        break;
                    }
                }
            }
            else{
                for(Component s: structuralComponents){
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
        if(!isValid(i, j)|| ship[i][j] == null || ship[i][j].getType() != ComponentType.CONTAINER) {
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
        //TODO: pensare se il giocatore può essere stupido o meno
        if ((!isValid(i, j)) || (ship[i][j] == null) || (!"BatteryHub".equals(ship[i][j].getType()))) {
            throw new IllegalArgumentException("This is not a battery hub: error in reduceBatteries of Board");
        } else {
            int indice = batteryHubs.indexOf(ship[i][j]);
            if (indice == -1) {
                throw new IllegalArgumentException("BatteryHub not found in 'batteryHubs' list: error in reduceBatteries of Board");
            } else {
                batteryHubs.get(indice).removeBatteries(num);
            }
        }
    }

    public void reduceCrew(int num, int i, int j){
        /* Dopo aver interagito con la UI, il player decide se togliere alieni o astronauti e da dove e per ogni posizione
       controller chiama reduceCrew specificandone la quantità e coordinate */
        if ((!isValid(i, j)) || (ship[i][j] == null) || (!"HousingUnit".equals(ship[i][j].getType()))) {
            throw new IllegalArgumentException("This is not an housing unit: error in reduceCrew of Board");
        } else {
            int indice = housingUnits.indexOf(ship[i][j]);
            if (indice == -1) {
                throw new IllegalArgumentException("HousingUnit not found in 'housingUnit' list: error in reduceCrew of Board");
            } else {
                housingUnits.get(indice).reduceOccupants(num);
            }
        }
        if(calculateCrew() == 0){
            //TODO: gestire il caso in cui il giocatore non ha più equipaggio ed è costretto ad abbandonare la corsa
            System.out.println("Player must leave game!");
        }
    }

    public int calculateExposedConnectors(){
        /* Fa dei controlli lato per lato, analizzando esclusivamente i component che definiscono il bordo della nave,
         * e controlla se ci sono dei controllori esposti. In quel caso aggiorno il contatore*/
        int count = 0;
        int rows = ship.length;
        int cols = ship[0].length;
        //SIDE UP
        for(int j = 0; j < cols; j++){
            for(int i = 0; i < rows; i++){
                if(isValid(i,j) && ship[i][j] != null){
                    if(ship[i][j].getUp().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getUp().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getUp().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getUp().equals(Side.SHIELD_DOUBLE_CONNECTOR)){
                        count ++;
                    }
                    break;
                }
            }
        }
        //SIDE DOWN
        for(int j = 0; j < cols ; j++){
            for(int i = rows - 1; i >= 0; i--){
                if(isValid(i,j) && ship[i][j] != null){
                    if(ship[i][j].getDown().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getDown().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getDown().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getDown().equals(Side.SHIELD_DOUBLE_CONNECTOR)){
                        count ++;
                    }
                    break;
                }
            }
        }
        //SIDE LEFT
        for(int i = 0; i < rows ; i++){
            for(int j = 0; j < cols; j++){
                if(isValid(i,j) && ship[i][j] != null){
                    if(ship[i][j].getLeft().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getLeft().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getLeft().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getLeft().equals(Side.SHIELD_DOUBLE_CONNECTOR)){
                        count ++;
                    }
                    break;
                }
            }
        }
        //SIDE RIGHT
        for(int i = 0; i < rows ; i++){
            for(int j = cols - 1; j >= 0; j--){
                if(isValid(i,j) && ship[i][j] != null){
                    if(ship[i][j].getRight().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getRight().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getRight().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getRight().equals(Side.SHIELD_DOUBLE_CONNECTOR)){
                        count ++;
                    }
                    break;
                }
            }
        }
        return count;
    }

    public double calculateCannonStrength() {
        /*  I cannoni singoli puntati in avanti contano +1, gli altri +½. Se spendi una batteria, i cannoni doppi puntati in avanti contano +2, gli altri +1.
        L’alieno viola conta +2, ma solo se la potenza di fuoco è già superiore a 0. */
        double strength = 0;
        int i = 0, j = 0;
        for (StructuralComponent gun : guns) {
            if ("DoubleGun".equals(gun.getType())) {
                if (calculateBatteriesAvailable() > 0) {
                    // TODO: chiedere al player se vuole spendere la batteria per questo cannone
                    if (playerSaysYes()) {
                        reduceBatteries(1, i, j); // scarica la batteria
                        if (gun.getUp().equals(Side.DOUBLE_GUN)) {
                            strength += 2;
                        } else {
                            strength += 1; // orientamento orizzontale: potenza dimezzata
                        }
                    }
                }
            } else if ("SingleGun".equals(gun.getType())) {
                if (gun.getUp().equals(Side.SINGLE_GUN)) {
                    strength += 1;
                } else {
                    strength += 0.5;
                }
            }
        }
        for(HousingUnit housing: housingUnits){
            if(housing.getAlien().equals(Color.Purple) && strength > 0){
                strength += 2;
                break;
            }
        }
        return strength;
    }

    public int calculateEngineStrength() {
        /*  I motori singoli contano +1. I motori doppi contano +2 se spendi una batteria. L’alieno marrone
        conta +2, ma solo se la potenza motrice è già superiore a 0. */
        int strength = 0;
        int i = 0, j = 0; //placeholder
        for (StructuralComponent engine : engines) {
            if ("DoubleEngine".equals(engine.getType())) {
                if (calculateBatteriesAvailable() > 0) {
                    // TODO: chiedere al player se vuole spendere la batteria per questo motore
                    if (playerSaysYes()) {
                        reduceBatteries(1, i, j); // scarica la batteria
                        strength += 2;
                    }
                }
            } else if ("SingleEngine".equals(engine.getType())) {
                strength += 1;
            }
        }
        for(HousingUnit housing: housingUnits){
            if(housing.getAlien().equals(Color.Brown) && strength > 0){
                strength += 2;
                break;
            }
        }
        return strength;
    }

    public int calculateBatteriesAvailable(){
        int numbatteries = 0;
        for(BatteryHub BatteryHub: batteryHubs){
            numbatteries += BatteryHub.getNumBatteries();
        }
        return numbatteries;
    }

    public int calculateCrew(){
        int crew = 0;
        for(HousingUnit housing: housingUnits){
            if(housing.getAlien() != null){
                crew += 1;
            }
            else{crew += housing.getNumAstronaut();}
        }
        return crew;
    }

    public Component[][] getShip(){
        return ship;
    }

// ciao sto provando altro ciao
// ok
}