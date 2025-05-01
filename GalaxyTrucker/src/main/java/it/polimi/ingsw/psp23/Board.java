package it.polimi.ingsw.psp23;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.*;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.ConnectorType;
import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.Side;

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
    private final ArrayList<Component> reservedTiles;
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
        reservedTiles = new ArrayList<>();
    }

    /**
     * Verifica se due lati sono compatibili ai fini del check().
     * A differenza di canConnect, considera anche EMPTY/SHIELD come compatibili tra loro.
     * @return true se i lati sono compatibili, false altrimenti
     */
    private boolean areSidesCompatible(Side a, Side b) {
        if (canConnect(a, b))
            return true;

        // EMPTY o SHIELD (ConnectorType.NONE) sono compatibili tra loro nel check.
        // Non saranno mai GUN/ENGINE perchè li controllo prima
        return a.connectorType() == ConnectorType.NONE && b.connectorType() == ConnectorType.NONE;
    }

    /**
     * Controlla la legalità della nave secondo le regole del gioco:
     * - Ogni modulo deve avere connessioni valide ai lati adiacenti
     * - Nessun motore o cannone deve essere puntato contro un altro modulo
     * - Tutte le tile devono essere raggiungibili dalla cabina centrale (2,3)
     * @return true se la nave è legalmente costruita, false altrimenti
     */
    public boolean check() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (!isFree(i, j)) {
                    if (isValid(i - 1, j) && !isFree(i - 1, j) && ship[i - 1][j].getDown() == Side.ENGINE) {
                        return false;
                    }

                    Component curr = ship[i][j];

                    // Un motore può essere orientato solo verso il basso
                    if (curr.getUp() == Side.ENGINE || curr.getLeft() == Side.ENGINE || curr.getRight() == Side.ENGINE)
                        return false;

                    // RIGHT
                    if (isValid(i, j + 1) && !isFree(i, j + 1)) {
                        Component right = ship[i][j + 1];
                        if (right.getLeft() == Side.GUN || right.getLeft() == Side.ENGINE)
                            return false;
                        if (!areSidesCompatible(curr.getRight(), right.getLeft()))
                            return false;
                    }

                    // LEFT
                    if (isValid(i, j - 1) && !isFree(i, j - 1)) {
                        Component left = ship[i][j - 1];
                        if (left.getRight() == Side.GUN || left.getRight() == Side.ENGINE)
                            return false;
                        if (!areSidesCompatible(curr.getLeft(), left.getRight()))
                            return false;
                    }

                    // DOWN
                    if (isValid(i + 1, j) && !isFree(i + 1, j)) {
                        Component down = ship[i + 1][j];
                        if (down.getUp() == Side.GUN || down.getUp() == Side.ENGINE)
                            return false;
                        if (!areSidesCompatible(curr.getDown(), down.getUp()))
                            return false;
                    }

                    // UP
                    if (isValid(i - 1, j) && !isFree(i - 1, j)) {
                        Component up = ship[i - 1][j];
                        if (up.getDown() == Side.GUN || up.getDown() == Side.ENGINE)
                            return false;
                        if (!areSidesCompatible(curr.getUp(), up.getDown()))
                            return false;
                    }

                    // Verifica raggiungibilità dalla cabina centrale
                    if (!isReachable(new boolean[5][7], 2, 3, i, j))
                        return false;
                }
            }
        }
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

    private void setTrue(boolean[][] m, int i, int j) {
        m[i][j] = true;
    }

    /**
     * Verifica se due connettori sono compatibili ai fini della raggiungibilità.
     * Viene usato da isReachable, quindi considera solo i veri connettori (esclude EMPTY, SHIELD, ENGINE, GUN).
     * @return true se i due connettori sono compatibili, false altrimenti
     */
    private boolean canConnect(Side a, Side b) {
        ConnectorType ca = a.connectorType();
        ConnectorType cb = b.connectorType();

        // Nessuna connessione possibile se uno dei due lati non è un connettore
        if (ca == ConnectorType.NONE || cb == ConnectorType.NONE)
            return false;

        // UNIVERSAL può connettersi a qualsiasi tipo, tranne NONE
        if (ca == ConnectorType.UNIVERSAL || cb == ConnectorType.UNIVERSAL)
            return true;

        // entrambi SINGLE o entrambi DOUBLE
        return ca == cb;
    }


    /** verifica se le due tile (x,y) e (nx,ny) sono unite da connettori compatibili */
    public boolean areTilesConnected(int x,int y,int nx,int ny){
        Component from = ship[x][y];
        Component to   = ship[nx][ny];

        if (from==null || to==null) return false;    // sicurezza

        if (nx == x+1 && ny == y)      // giù
            return canConnect(from.getDown(), to.getUp());
        if (nx == x-1 && ny == y)      // su
            return canConnect(from.getUp(),   to.getDown());
        if (nx == x && ny == y+1)    // destra
            return canConnect(from.getRight(),to.getLeft());
        if (nx == x && ny == y-1)    // sinistra
            return canConnect(from.getLeft(), to.getRight());

        return false;
    }

    /** Il ragionamento di questo metodo è: partendo dal modulo centrale, controlla che il componente in posizione i e j
     che stiamo cercando non sia l'adiacente, se non è l'adiacente, fai una chiamata ricorsiva sui moduli
     * @param alreadyChecked matrice che indica le celle già visitate
     * @param modCentrX coordinata X da cui parte la ricerca
     * @param modCentrY coordinata Y da cui parte la ricerca
     * @param i coordinata X da raggiungere
     * @param j coordinata Y da raggiungere
     * @return True se la tile in ship[i][j] è raggiungibile da ship[modCentrX][modCentrY]
     */
    private boolean isReachable(boolean[][] alreadyChecked, int modCentrX, int modCentrY, int i, int j) {
        // inizializzo scorrX e scorrY alle coordinate del modulo centrale del livello due
        int scorrX = modCentrX;
        int scorrY = modCentrY;
        boolean check1 = false, check2 = false, check3 = false, check4 = false;

        // segno la cella come visitata
        setTrue(alreadyChecked, scorrX, scorrY);

        // la cella raggiunta è quella di interesse -> è raggiungibile
        if (i == scorrX && j == scorrY) {
            return true;
        }
        // condizione di visita al nodo adiacente
        if (isValid(scorrX + 1, scorrY) && !isFree(scorrX+1, scorrY) && !alreadyChecked[scorrX + 1][scorrY] && areTilesConnected(scorrX, scorrY, scorrX+1, scorrY)) {
            if (scorrX + 1 == i && scorrY == j)
                return true;
            else
                check1 = isReachable(alreadyChecked, scorrX + 1, scorrY, i, j);
        }

        if (isValid(scorrX - 1, scorrY) && !isFree(scorrX-1, scorrY)  && !alreadyChecked[scorrX - 1][scorrY] && areTilesConnected(scorrX, scorrY, scorrX-1, scorrY)) {
            if (scorrX - 1 == i && scorrY == j)
                return true;
            else
                check2 = isReachable(alreadyChecked, scorrX - 1, scorrY, i, j);
        }

        if (isValid(scorrX, scorrY + 1) && !isFree(scorrX, scorrY+1) && !alreadyChecked[scorrX][scorrY + 1] && areTilesConnected(scorrX, scorrY, scorrX, scorrY+1)) {
            if (scorrX == i && scorrY + 1 == j)
                return true;
            else
                check3 = isReachable(alreadyChecked, scorrX, scorrY + 1, i, j);
        }

        if (isValid(scorrX, scorrY - 1) && !isFree(scorrX, scorrY-1) && !alreadyChecked[scorrX][scorrY - 1] && areTilesConnected(scorrX, scorrY, scorrX, scorrY-1)) {
            if (scorrX == i && scorrY - 1 == j)
                return true;
            else
                check4 = isReachable(alreadyChecked, scorrX, scorrY - 1, i, j);
        }

        return (check1 || check2 || check3 || check4);
    }


    // elimina elemento in posizione i,j della ship e lo rimuove dalla rispettiva lista
    // funziona perchè ship[i][j] e l'elemento della rispettiva lista puntano allo stesso oggetto in memoria:
    // la prima lo "vede" come Component (sovraclasse), mentre la lista lo vede come oggetto della sottoclasse.
    /** Grazie al sealed pattern matching, il compilatore riconosce il tipo effettivo dell'oggetto
     * e lo assegna alla variabile del case senza bisogno di instanceof o cast manuali. */
    public void delete(int i, int j) {
        if (!isValid(i, j) || isFree(i, j))
            throw new InvalidCoordinatesException("There isn't any component in this slot or your indexes are invalid: exception in delete(i,j) of Board");

        /* Nelle ArrayList il metodo remove ha due implementazioni: uso quella che riceve un oggetto e rimuove il primo elemento
           su cui equals restituisce true: non serve override perchè equals confronta i riferimenti (in questo caso sono gli stessi). Controllo che esista lo stesso oggetto e venga rimosso correttamente.  */
        boolean removed = switch (ship[i][j]) {
            case AlienAddOns a -> alienAddOns.remove(a);
            case BatteryHub batteryHub -> batteryHubs.remove(batteryHub);
            case Container container -> containers.remove(container);
            case HousingUnit cabin -> housingUnits.remove(cabin);
            case Cannon cannon -> cannons.remove(cannon);
            case Engine engine -> engines.remove(engine);
            case Shield shield -> shields.remove(shield);
            case StructuralComponent tube -> structuralComponents.remove(tube);
            default -> false;
        };

        if (!removed) {
            throw new ComponentMismatchException("Componente" +
                    " non trovato nella rispettiva lista: errore in delete() di Board alla cella [" + i + "][" + j + "]");
        }

        // elimino component e aggiorno la pila degli scarti
        ship[i][j] = null;
        garbage++;

        // adesso elimino i pezzi che non sono più raggiungibili, ricominciando il ciclo ogni volta che ne trovo uno
        // perchè potrebbe essere importante per collegare altri componenti
        // TODO: manca da analizzare il caso in cui venga eliminata il modulo centrale
        for (int row = 0; row < ship.length; row++) {
            for (int col = 0; col < ship[row].length; col++) {
                // 2,3 sono le coordinate della cabina centrale
                if (isValid(row,col) && !isFree(row,col)) {
                    // ogni ricerca di raggiungibilità deve avere matrice dei visitati "pulita" (non "sporca da ricerche precedenti)
                    boolean[][] visited = new boolean[ROWS][COLS];

                    if (!isReachable(visited, 2, 3, row, col)) {
                        delete(row, col);
                        row = 0;
                        col = 0;
                    }
                }
            }
        }
    }

    public void reserveTile(Component c) {
        if (reservedTiles.size() >= 2)
            throw new InvalidComponentActionException("Puoi prenotare al massimo due tiles!");
        c.reserve();
        reservedTiles.add(c);
    }

    public ArrayList<Component> getReservedTiles() {
        return reservedTiles;
    }

    private boolean hasAdjacentTile(int i, int j) {
        // verifico se vi sia almeno un componente adiacente in una posizione valida (controllo necessario per saldatura tile)
        return (isValid(i - 1, j) && !isFree(i - 1, j)) ||
                (isValid(i + 1, j) && !isFree(i + 1, j)) ||
                (isValid(i, j - 1) && !isFree(i, j - 1)) ||
                (isValid(i, j + 1) && !isFree(i, j + 1));
    }

    public void addComponent(Component c, int i, int j) {
        // solo la cabina centrale non avrà tile adiacenti al momento dell'inserimento; tutte le altre avranno questo vincolo.
        // Inoltre, la posizione deve essere valida e libera
        if ( !isValid(i, j) || !isFree(i, j) || (!hasAdjacentTile(i, j) && !c.isStartingCabin()))
            throw new InvalidCoordinatesException("You can't add this component: invalid parameters i and j");

        if (getReservedTiles().contains(c))
            reservedTiles.remove(c);

        ship[i][j] = c;
        c.setX(i);
        c.setY(j);
        c.placeOnTruck();
        // aggiungo nella rispettiva lista in base al tipo. I due oggetti (in ship e nella lista) avranno
        // lo stesso riferimento, ma verranno visti da "due punti di vista diversi"
        switch (c) {
            case AlienAddOns a -> alienAddOns.add(a);
            case Cannon cannon -> cannons.add(cannon);
            case BatteryHub batteryHub -> batteryHubs.add(batteryHub);
            case Container container -> containers.add(container);
            case HousingUnit cabin -> housingUnits.add(cabin);
            case Engine engine -> engines.add(engine);
            case StructuralComponent tube -> structuralComponents.add(tube);
            case Shield shield -> shields.add(shield);
            default -> {}
        }
    }

    /**
     * @param item è l'oggetto che l'utente desidera rimuovere
     * @return true se l'item è tra i più preziosi disponibili, false altrimenti
     */
    public boolean isMostPrecious(Item item) {
        Color[] preciousness = {Color.Red, Color.Yellow, Color.Green, Color.Blue};

        for (Color priority_color : preciousness) {
            if (priority_color == item.getColor()) {
                // abbiamo raggiunto il colore dell’item: nessuna merce più preziosa trovata
                return true;
            }
            for (Container container : containers) {
                for (Item current_item : container.getItems()) {
                    if (current_item.getColor() == priority_color) {
                        // esiste almeno una merce più preziosa
                        return false;
                    }
                }
            }
        }
        // si eseguiranno sempre e solo i due return precedenti. L'ho messo per evitare error "missing return statement"
        return true;
    }

//    /**
//     * Rimuove una merce da un container in posizione (i,j), solo se risulta tra le più preziose ancora presenti a bordo.
//     * @param i coordinata riga del container
//     * @param j coordinata colonna del container
//     * @param itemToRemove oggetto Item che il giocatore ha scelto di rimuovere
//     */
//    public void removePreciousItemFromContainer(int i, int j, Item itemToRemove) {
//
//        // Controllo che l'item sia tra i più preziosi attualmente a bordo
//        if (!isMostPrecious(itemToRemove))
//            throw new IllegalArgumentException("Item" + itemToRemove.getColor() + " at Container[" + i + "][" + j + "] is not among the most precious: you must remove the most valuable item first.");
//
//        Component tile = ship[i][j];
//        switch (tile) {
//            case Container c -> {
//                // Trovo l'indice del container corrispondente a ship[i][j] nella lista dei container
//                // L'oggetto in ship[i][j] è lo stesso oggetto (stesso riferimento) inserito in containers, quindi indexOf funziona correttamente.
//                int index = containers.indexOf(ship[i][j]);
//                // Controllo che l'indice sia valido: se è -1, significa che ship[i][j] non è un container noto
//                if (index == -1) {
//                    throw new ComponentMismatchException("Invalid coordinates: ship[i][j] does not contain a container.");
//                }
//                // provo a rimuovere item: se loseItem lancia eccezione, la raccolgo e la rilancio con contesto affinchè venga gestita meglio dal controller
//                try {
//                    containers.get(index).loseItem(itemToRemove);
//                }
//                catch (ContainerException e) {
//                    throw new ContainerException("Cannon remove precious item in Container at Ship["+i+"]["+j+"]:" + e.getMessage());
//                }
//            }
//            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not a container");
//        }
//    }


    public void handleCannonShot(CannonShot cannonShot, int impactLine) {

        /* Se la cannonata è grande, distrugge sempre il primo componente colpito.
        La plancia del livello 2 ha coordinate di gioco diverse dalla matrice interna,
        quindi convertiamo impactLine negli indici reali prima di applicare l'effetto. */

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
            // cannonata piccola
            boolean isCovered = false;
            // Se lo scudo è stato attivato, controllo se copre la direzione da cui arriva la cannonata
            for (Shield s : shields) {
                if(s.isActive()) {
                    s.disactiveShield();
                    Direction dir = cannonShot.getDirection();
                    // In base alla direzione da cui arriva la cannonata, seleziono il lato dello scudo corrispondente
                    Side sideDir = switch (dir) {
                        case UP -> s.getUp();
                        case DOWN -> s.getDown();
                        case LEFT -> s.getLeft();
                        case RIGHT -> s.getRight();
                    };
                    // Se quel lato dello scudo è effettivamente uno scudo (e non un connettore), allora siamo coperti
                    if (sideDir.isShield()) {
                        isCovered = true;
                        break;
                    }
                }
            }

            // utente ha scelto di non coprirsi (o ha sbagliato a coprirsi) da cannonata piccola
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
            // se lo scudo è stato attivato, controllo se copre la direzione da cui arriva la meteora
            for (Shield s : shields) {
                if(s.isActive()){
                    s.disactiveShield();
                    Direction dir = meteor.getDirection();
                    // In base alla direzione da cui arriva la meteora, seleziono il lato dello scudo corrispondente
                    Side sideDir = switch (dir) {
                        case UP -> s.getUp();
                        case DOWN -> s.getDown();
                        case LEFT -> s.getLeft();
                        case RIGHT -> s.getRight();
                    };
                    // Se quel lato dello scudo è effettivamente uno scudo (e non un connettore), allora siamo coperti
                    if (sideDir.isShield()) {
                        isCovered = true;
                        break;
                    }
                }
            }

            // Utente ha scelto di non coprirsi (o ha sbagliato a coprirsi) da meteora piccola.
            // Appena trovo un componente valido sulla colonna/linea di impatto:
            // - Se ha connettore NONE (quindi non un connettore) nella direzione d'impatto, la meteora rimbalza → esco subito (break)
            // - Altrimenti il modulo ha un connettore esposto → lo elimino
            if (!isCovered) {
                if (meteor.getDirection() == Direction.UP) {
                    for (int i = 0; i < ship.length; i++) {
                        if (isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            if (ship[i][realImpactLine].getUp().connectorType() != ConnectorType.NONE) {
                                delete(i, realImpactLine);
                            }
                            break;
                        }
                    }
                } else if (meteor.getDirection() == Direction.DOWN) {
                    for (int i = ship.length - 1; i >= 0; i--) {
                        if (isValid(i, realImpactLine) && !isFree(i, realImpactLine)) {
                            if (ship[i][realImpactLine].getDown().connectorType() != ConnectorType.NONE) {
                                delete(i, realImpactLine);
                            }
                            break;
                        }
                    }
                } else if (meteor.getDirection() == Direction.RIGHT) {
                    for (int j = ship[realImpactLine].length - 1; j >= 0; j--) {
                        if (isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                            if (ship[realImpactLine][j].getRight().connectorType() != ConnectorType.NONE) {
                                delete(realImpactLine, j);
                            }
                            break;
                        }
                    }
                } else {
                    for (int j = 0; j < ship[realImpactLine].length; j++) {
                        if (isValid(realImpactLine, j) && !isFree(realImpactLine, j)) {
                            if (ship[realImpactLine][j].getLeft().connectorType() != ConnectorType.NONE) {
                                delete(realImpactLine, j);
                            }
                            break;
                        }
                    }
                }
            }

        } else {
            /* Per meteore verticali, cerco un cannone (singolo o doppio) nella stessa colonna e rivolto nella direzione corretta.
               Per meteore orizzontali, verifico la presenza di cannoni adiacenti lungo la linea d’impatto.
               Non controllo eventuali ostacoli davanti al cannone: si assume che la nave sia costruita correttamente. */

            // Flag che indica se la meteora è stata distrutta da un cannone
            boolean isDestroyed = false;
            // Scorro tutti i cannoni installati sulla nave
            for (Cannon s : cannons) {
                // Determino se il cannone si trova in una posizione adatta a colpire la meteora
                boolean matchesPosition = switch (meteor.getDirection()) {
                    case UP -> s.getY() == realImpactLine;
                    case DOWN -> s.getY() == realImpactLine || s.getY() == realImpactLine + 1 || s.getY() == realImpactLine - 1;
                    case RIGHT, LEFT ->
                            s.getX() == realImpactLine || s.getX() == realImpactLine + 1 || s.getX() == realImpactLine - 1;
                };
                // (L'ho svolto così per evitare quadruplicazione cospicua di codice).
                // Verifico se il cannone è rivolto nella direzione da cui arriva la meteora
                boolean facesCorrectDirection = switch (meteor.getDirection()) {
                    case UP -> s.getUp() == Side.GUN;
                    case DOWN -> s.getDown() == Side.GUN;
                    case RIGHT -> s.getRight() == Side.GUN;
                    case LEFT -> s.getLeft() == Side.GUN;
                };

                // Se il cannone è in posizione corretta e rivolto nel verso giusto
                if (matchesPosition && facesCorrectDirection) {
                    if (s.isDouble()) {
                        // Se il cannone è doppio, può sparare solo se è stato attivato dal Controller
                        if (s.isActive()) {
                            isDestroyed = true;          // Meteora distrutta
                            s.disactiveCannon();         // Disattivo il cannone dopo l’uso
                            break;                       // Esco dal ciclo: non serve cercare altri cannoni
                        }
                    } else {
                        // I cannoni singoli possono sparare senza attivazione
                        isDestroyed = true;
                        break;                           // Meteora distrutta, interrompo la ricerca
                    }
                }
            }

            // se la meteor non è stata distrutta ho impatto: devo rimuovere
            if (!isDestroyed) {
                // distinguo in base alle direzioni perchè devo scorrere la matrice in modo diverso
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

    /**
     Il metodo loadGoods carica una lista di item in un singolo container in posizione (i,j).
     Si assume che la lista sia stata già validata dal Controller come carico destinato a quel container specifico.
     Il metodo prova a caricare ogni item e, se anche uno solo non rispetta le regole (colore o spazio disponibile),
     viene lanciata un'eccezione (presente in loadItem) e nessun item successivo viene caricato (ma i preceedenti si!).
     Eventuali scelte strategiche o ripartizioni su più container devono essere gestite dal Controller.
     */
    public void loadGoods(List<Item> items, int i, int j) {
        if (!isValid(i, j) || isFree(i,j))
            throw new InvalidCoordinatesException("Coordinates("+i+","+j+") cannon contain a tile or don't contain one");

        Component tile = ship[i][j];
        switch (tile) {
            case Container container -> {
                int index = containers.indexOf(container);
                if (index == -1) {
                    throw new ComponentMismatchException("Container not found in 'containers' list: error in loadGoods of Board");
                }

                int scorr = 0;
                while (scorr < items.size()) {
                    try {
                        // loadItem controlla anche se l'item può essere caricato in quello specifico container
                        containers.get(index).loadItem(items.get(scorr));
                        scorr++;
                    } catch (ContainerException c) {
                        // Rilancio una ContainerException con maggior contesto, da gestire poi nel Controller
                        throw new ContainerException("Item at index " + scorr + " cannot be loaded in container at [" + i + "][" + j + "]: " + c.getMessage());
                    }
                }
            }
            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not a container");
        }
    }


    public void updateAllowedAliens() {
        /* per ogni housingunit faccio un ciclo che controlli eventuali addons adiacenti, per poi aggiungere
           il colore dell'alienaddon corrispondente; non ho messo un break nel for
           quando trovo un addon perchè potrei avere più addons e quindi avere a disposizione più colori*/
        for (HousingUnit h : housingUnits) {
            for (AlienAddOns a : alienAddOns) {
                // se trovo un AddOns adiacente lo aggiungo alla lista dei supporti vitali adiacenti alla HousingUnit
                if ((a.getX() == h.getX() + 1 && a.getY() == h.getY()) || (a.getX() == h.getX() - 1 && a.getY() == h.getY()) || (a.getY() == h.getY() + 1 && a.getX() == h.getX()) || (a.getY() == h.getY() - 1 && a.getX() == h.getX())) {
                    h.addConnectedAddon(a.getColor());
                }
            }
        }
    }

    /**
     * Checks if the board already contains an alien of the specified color.
     * According to the rules, each ship can have at most one alien per color.
     * @param color the color of the alien to check
     * @return true if an alien of the same color is already present, false otherwise
     */
    public boolean containsSameAlien(Color color) {
        for (HousingUnit h : housingUnits) {
            if (h.getAlien() == color)
                return true;
        }
        return false;
    }


    public void reduceBatteries(int i, int j, int num) {
        if ((!isValid(i, j)) || isFree(i, j))
            throw new InvalidCoordinatesException("Coordinates("+i+","+j+") cannon contain a tile or don't contain one");

        Component tile = ship[i][j];
        switch (tile) {
            case BatteryHub batteryHub -> {
                int index = batteryHubs.indexOf(batteryHub);
                if (index == -1) {
                    throw new ComponentMismatchException("BatteryHub did not found in batteryHubs for Ship[" + i + "][" + j + "]");
                }
                try {
                    // controllo su numero batterie è gestito in removeBatteries
                    batteryHubs.get(index).removeBatteries(num);
                } catch (IllegalArgumentException e) {
                    throw new BatteryOperationException("BatteryHub at Ship["+i+"]["+j+"]" + e.getMessage());
                }
            }
            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not a battery hub");
        }
    }


//    public void reduceCrew(int i, int j, int num) {
//        if ((!isValid(i, j)) || isFree(i,j))
//            throw new InvalidCoordinatesException("Coordinates("+i+","+j+") cannon contain a tile or don't contain one");
//
//        Component tile = ship[i][j];
//        switch (tile) {
//            case HousingUnit cabin -> {
//                int index = housingUnits.indexOf(cabin);
//                if (index == -1) {
//                    throw new ComponentMismatchException("HousingUnit not found in 'housingUnit' list: error in reduceCrew of Board");
//                } else {
//                    try {
//                        // controllo rimozione implementato in reduceOccupants
//                        housingUnits.get(index).reduceOccupants(num);
//                    }
//                    catch (IllegalArgumentException e) {
//                        throw new CrewOperationException("Failed to remove "+ num + "crew members from HousingUnit at Ship["+i+"]["+j+"]" + e.getMessage());
//                    }
//                }
//            }
//            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not a housing unit");
//        }
//    }

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

    public void activeCannon(int i, int j) {
        if ((!isValid(i, j)) || isFree(i,j))
            throw new InvalidCoordinatesException("Coordinates("+i+","+j+") cannon contain a tile or don't contain one");

        Component tile = ship[i][j];
        switch (tile) {
            case Cannon cannon -> {
                int index = cannons.indexOf(cannon);
                if (index == -1)
                    throw new ComponentMismatchException("Cannons list does not contain the cannon at Ship["+i+"]["+j+"]");
                if(!cannons.get(index).isDouble())
                    throw new InvalidComponentActionException("The cannon at Ship["+i+"]["+j+"] is not a double cannon!");
                else
                    cannons.get(index).activeCannon();
            }
            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not a cannon");
        }
    }

    public void activeShield(int i, int j) {
        if ((!isValid(i, j)) || isFree(i,j))
            throw new InvalidCoordinatesException("Coordinates("+i+","+j+") shield contain a tile or don't contain one");

        Component tile = ship[i][j];
        switch (tile) {
            case Shield shield -> {
                int index = shields.indexOf(shield);
                if (index == -1)
                    throw new ComponentMismatchException("Shields list does not contain the shield at Ship[" + i + "][" + j + "]");
                shields.get(index).activeShield();
            }
            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not a shield");
        }
    }

    public void activeEngine(int i, int j) {
        if ((!isValid(i, j)) || isFree(i,j))
            throw new InvalidCoordinatesException("Ship[" + i + "][" + j + "] does not contain a valid Housing Unit.");

        Component tile = ship[i][j];
        switch (tile) {
            case Engine engine -> {
                int index = engines.indexOf(engine);
                if (index == -1)
                    throw new ComponentMismatchException("Engines list does not contain the engine at Ship["+i+"]["+j+"]");
                if(!engines.get(index).isDouble())
                    throw new InvalidComponentActionException("The engine at Ship["+i+"]["+j+"] is  not a double engine!");
                else
                    engines.get(index).activeEngine();
            }
            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not an engine");
        }
    }

    public double calculateCannonStrength() {

        // I cannoni singoli puntati in avanti contano +1, gli altri +½. Se spendi una batteria, i cannoni doppi puntati in avanti contano +2, gli altri +1.
        double strength = 0;
        for (Cannon gun : cannons) {
            if (gun.getUp().equals(Side.GUN)) {
                if(gun.isDouble()){
                    if(gun.isActive()){
                        // cannone doppio conta solo se è stato attivato prima dall'utente tramite activeCannon(i,j)
                        strength += 2;
                        gun.disactiveCannon();   // lo disattivo: attivazione è "monouso"
                    }
                }
                else{
                    strength += 1;
                }
            } else {
                if(gun.isDouble()){
                    if(gun.isActive()){
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
                if (housing.getAlien() != null && housing.getAlien().equals(Color.Purple)) {
                    strength += 2;
                    break;  // esco perchè può esserci solo un alieno viola
                }
            }
        }
        return strength;
    }

    public int calculateEngineStrength() {
        //  I motori singoli contano +1. I motori doppi contano +2 se spendi una batteria.
        int strength = 0;
        for (Engine engine : engines) {
            if(engine.isDouble()){
                if(engine.isActive()) {
                    // motore doppio conta solo se è stato attivato
                    strength += 2;
                    engine.disactiveEngine();   // lo disattivo: attivazione è "monouso"
                }
            }
            else{
                strength += 1;
            }
        }

        // L’alieno marrone conta +2, ma solo se la potenza motrice è già superiore a 0.
        if (strength > 0) {
            for (HousingUnit housing : housingUnits) {
                if (housing.getAlien() != null && housing.getAlien().equals(Color.Brown)) {
                    strength += 2;
                    break;   // esco perchè può esserci solo un alieno marrone
                }
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

    /** @return the total number of human crew members */
    public int calculateHumanCrew() {
        int humans = 0;
        for (HousingUnit h : housingUnits) {
            if (h.getAlien() == null) {
                humans += h.getNumAstronaut();
            }
        }
        return humans;
    }

    /** @return the total number of aliens on board*/
    public int calculateAlienCrew() {
        int aliens = 0;
        for (HousingUnit h : housingUnits) {
            if (h.getAlien() != null) {
                aliens += 1;
            }
        }
        return aliens;
    }

    /** @return  total crew members (humans + aliens) */
    public int calculateCrew() {
        return calculateHumanCrew() + calculateAlienCrew();
    }


    /**
     * @return somma totale in crediti ricavati dalle merci.
     * */
    public int calculateGoodsSales() {
        int money = 0;
        for (Container c : containers) {
            for (Item item: c.getItems()) {
                money += switch(item.getColor()) {
                    case Red -> 4;
                    case Yellow -> 3;
                    case Green -> 2;
                    case Blue -> 1;
                    // aggiungo questi casi per evitare error di java "switch-case does not cover all input values"
                    case Brown -> 0;
                    case Purple -> 0;
                };
            }
        }
        return money;
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

    public int getGarbage() {
        return garbage;
    }

    /** Di seguito metto metodi necessari per il testing (BoardTest) */
    public List<Cannon> getCannons() {
        // ritorno copia immutabile così è solo read-only ed evito side-effects esterni
        return List.copyOf(cannons);
    }

    public List<Container> getContainers() {
        return List.copyOf(containers);
    }

    public List<Engine> getEngines() {
        return List.copyOf(engines);
    }

    public List<AlienAddOns> getAlienAddOns() {
        return List.copyOf(alienAddOns);
    }

    public List<HousingUnit> getHousingUnits() {
        return List.copyOf(housingUnits);
    }

    public Component getTile(int i, int j) {
        return ship[i][j];
    }

    public Component[][] getShip() {
        return ship;
    }
}
