package it.polimi.ingsw.psp23.model.Game;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.cards.*;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.*;
import it.polimi.ingsw.psp23.network.UsersConnected;

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
    int[][] validCoords;
    // username del player a cui appartiene la board
    String username;

    public Board(int level, String username) {
        if (level == 2) {
            validCoords = new int[][]{
                    {0, 2}, {0, 4}, {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4},
                    {2, 5}, {2, 6}, {3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {4, 0}, {4, 1}, {4, 2}, {4, 4}, {4, 5}, {4, 6}
            };
        }
        else {
            validCoords = new int[][]{
                    {0, 3}, {1, 2}, {1, 3}, {1, 4}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5},
                    {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {4, 1}, {4, 2}, {4, 4}, {4, 5},
            };
        }
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
        this.username = username;
    }

    /**
     * Determines if two sides are compatible based on their connection rules.
     * Compatibility is checked using the `canConnect` method, as well as ensuring
     * that both sides have a `ConnectorType` of NONE when they are empty or shields.
     *
     * @param a the first side to be checked for compatibility
     * @param b the second side to be checked for compatibility
     * @return true if the two sides are compatible, false otherwise
     */
    private boolean areSidesCompatible(Side a, Side b) {
        if (canConnect(a, b))
            return true;

        // EMPTY o SHIELD (ConnectorType.NONE) sono compatibili tra loro nel check.
        // Non saranno mai GUN/ENGINE perchè li controllo prima
        return a.connectorType() == ConnectorType.NONE && b.connectorType() == ConnectorType.NONE;
    }

    /**
     * Checks the validity and compatibility of all components in the ship's grid.
     * This includes verifying that engines and guns are appropriately positioned,
     * that adjacent components have compatible sides, and that all components are
     * reachable from the central cabin.
     *
     * @return true if all components in the ship's grid are correctly placed, compatible,
     *         and reachable, false otherwise
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


    /**
     * Determines if the specified position (i, j) can contain a component on the board.
     * The board does not have a perfectly matrix-like shape, so certain positions may be invalid.
     *
     * @param i the row index to be checked
     * @param j the column index to be checked
     * @return true if the position (i, j) can contain a component, false otherwise
     */
    public boolean isValid(int i, int j) {
        // estraggo la coppia corrente e controllo se matcha
        for (int[] coord : validCoords) {
            if (coord[0] == i && coord[1] == j) {
                return true;
            }
        }
        return false;
    }

    public int[][] getValidCoords() {
        return validCoords;
    }

    /**
     * Determines whether the specified position in the ship's grid is free,
     * meaning it does not contain any component.
     *
     * @param i the row index of the position to be checked
     * @param j the column index of the position to be checked
     * @return true if the position at (i, j) is free, false otherwise
     */
    public boolean isFree(int i, int j) {
        return ship[i][j] == null;
    }

    private void setTrue(boolean[][] m, int i, int j) {
        m[i][j] = true;
    }

    /**
     * Determines if two sides can connect based on their connector types.
     * The method evaluates compatibility rules, including specific behaviors
     * for universal connectors and ensuring that non-connector types cannot connect.
     *
     * @param a the first side to check for compatibility
     * @param b the second side to check for compatibility
     * @return true if the two sides can connect, false otherwise
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


    /**
     * Determines if two tiles on the board are connected based on their positions and compatibility.
     * The method checks the adjacency and connection between the tiles at the specified coordinates.
     *
     * @param x the X coordinate of the first tile
     * @param y the Y coordinate of the first tile
     * @param nx the X coordinate of the second, potentially adjacent tile
     * @param ny the Y coordinate of the second, potentially adjacent tile
     * @return true if the two tiles are connected, false otherwise
     */
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

    /**
     * Determines if a specific cell is reachable from the starting point in a grid.
     *
     * @param alreadyChecked a 2D array representing already visited cells in the grid
     * @param modCentrX the x-coordinate of the starting (central) position
     * @param modCentrY the y-coordinate of the starting (central) position
     * @param i the x-coordinate of the target cell to check for reachability
     * @param j the y-coordinate of the target cell to check for reachability
     * @return true if the target cell (i, j) is reachable from the central cell (modCentrX, modCentrY), otherwise false
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



    /**
     * Deletes a component located at the specified coordinates on the board.
     * The method checks the validity of the provided indices and ensures the
     * slot is not empty before attempting to remove the component. If the removal
     * is unsuccessful, appropriate exceptions are thrown. This method also updates
     * related structures and ensures disconnected components are recursively removed
     * and updates the garbage if the game is level 2.
     *
     * @param i the row index of the component to be deleted
     * @param j the column index of the component to be deleted
     * @throws InvalidCoordinatesException if the specified indices are invalid
     *         or there is no component at the given coordinates
     * @throws ComponentMismatchException if the component at the specified
     *         coordinates cannot be found in the corresponding list
     */
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

        // elimino component e aggiorno la pila degli scarti se necessario
        ship[i][j] = null;
        if (!(UsersConnected.getInstance().getGameFromUsername(username).getGameStatus() == GameStatus.CheckBoards && UsersConnected.getInstance().getGameFromUsername(username).getLevel() == 0)) {
            garbage++;
        }

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

    /**
     * Reserves a tile on the board for the specified component. A maximum of two tiles
     * can be reserved at a time. If the limit is exceeded, an exception is thrown.
     * This operation marks the component as reserved and adds it to the reserved tiles list.
     *
     * @param c the component to be reserved
     * @throws InvalidComponentActionException if more than two tiles are reserved
     */
    public void reserveTile(Component c) {
        if (reservedTiles.size() >= 2)
            throw new InvalidComponentActionException("Puoi prenotare al massimo due tiles!");
        c.reserve();
        reservedTiles.add(c);
    }

    public ArrayList<Component> getReservedTiles() {
        return reservedTiles;
    }

    /**
     * Checks if there is at least one adjacent tile in a valid position.
     * This method is necessary for determining if a tile can be joined with others.
     *
     * @param i the row index of the tile to check
     * @param j the column index of the tile to check
     * @return true if there is at least one adjacent tile in a valid position, false otherwise
     */
    private boolean hasAdjacentTile(int i, int j) {
        // verifico se vi sia almeno un componente adiacente in una posizione valida (controllo necessario per saldatura tile)
        return (isValid(i - 1, j) && !isFree(i - 1, j)) ||
                (isValid(i + 1, j) && !isFree(i + 1, j)) ||
                (isValid(i, j - 1) && !isFree(i, j - 1)) ||
                (isValid(i, j + 1) && !isFree(i, j + 1));
    }

    /**
     * Adds a component to the ship at the specified coordinates. Ensures that the position
     * is valid, free, and satisfies the adjacency or starting cabin constraints.
     * If the component is already reserved, it will be removed from the reserved tiles.
     * The component is placed in the specified position and added to its respective category list.
     *
     * @param c the component to be added to the ship
     * @param i the row index where the component will be placed
     * @param j the column index where the component will be placed
     * @throws InvalidCoordinatesException if the specified position is invalid, not free, or
     *                                     does not satisfy adjacency or starting cabin constraints
     */
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
     * Determines whether an item is the most precious on the ship based on its color.
     * The preciousness is determined by a predefined order of colors
     * which is Red,Yellow,Green, Blue
     *
     * @param item the item to be checked for preciousness
     * @return true if the specified item is the most precious, otherwise false
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

    /**
     * Removes a specified precious item from a container located at the given coordinates on the ship.
     * Validates that the component at the specified coordinates is a container and that the item is among
     * the most precious items on board before removal.
     *
     * @param i the row index of the ship matrix where the container is located
     * @param j the column index of the ship matrix where the container is located
     * @param item the 1-based index of the precious item to be removed from the container
     * @throws ComponentMismatchException if the component at the given coordinates is not a container or an invalid index for the container is provided
     * @throws IllegalArgumentException if the item to be removed is not among the most precious items
     * @throws ContainerException if there is an issue with removing the item from the container
     * @throws TypeMismatchException if the component at the given coordinates is not of the expected type (e.g., not a container)
     */
    public void removePreciousItem(int i, int j, int item) {
        Component tile = ship[i][j];
        switch (tile) {
            case Container c -> {
                if (item <= 0) {
                    throw new ComponentMismatchException("Invalid index of item. Please insert a positive index");
                }
                // Trovo l'indice del container corrispondente a ship[i][j] nella lista dei container
                // L'oggetto in ship[i][j] è lo stesso oggetto (stesso riferimento) inserito in containers, quindi indexOf funziona correttamente.
                int index = containers.indexOf(ship[i][j]);
                // Controllo che l'indice sia valido: se è -1, significa che ship[i][j] non è un container noto
                if (index == -1) {
                    throw new ComponentMismatchException("Invalid coordinates: ship[i][j] does not contain a container.");
                }
                Item itemToRemove = containers.get(index).getItems().get(item - 1);
                // Controllo che l'item sia tra i più preziosi attualmente a bordo
                if (!isMostPrecious(itemToRemove))
                    throw new IllegalArgumentException("Item" + itemToRemove.getColor() + " at Container[" + i + "][" + j + "] is not among the most precious: you must remove the most valuable item first.");
                // provo a rimuovere item: se loseItem lancia eccezione, la raccolgo e la rilancio con contesto affinchè venga gestita meglio dal controller
                try {
                    containers.get(index).loseItem(itemToRemove);
                }
                catch (ContainerException e) {
                    throw new ContainerException("Cannon remove precious item in Container at Ship["+i+"]["+j+"]:" + e.getMessage());
                }
            }
            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not a container");
        }
    }


    /**
     * Handles the impact of a cannon shot on a target, determining its effects
     * based on the direction, size of the shot, and whether a shield is active.
     * If the shot hits a valid target, it destroys the target or triggers the shield.
     *
     * This method adjusts the impact location based on the game's internal coordinate system
     * and processes the effect differently for big and small cannon shots.
     *
     * @param cannonShot The cannon shot being processed, containing information about its size and direction.
     * @param impactLine The line or axis of impact, which is adjusted to align with the internal game grid.
     */
    public void handleCannonShot(CannonShot cannonShot, int impactLine) {

        /* Se la cannonata è grande, distrugge sempre il primo componente colpito.
        La plancia del livello 2 ha coordinate di gioco diverse dalla matrice interna,
        quindi convertiamo impactLine negli indici reali prima di applicare l'effetto. */

        int realImpactLine;
        if (cannonShot.getDirection() == Direction.UP || cannonShot.getDirection() == Direction.DOWN) {
            realImpactLine = impactLine - 4;
            if (realImpactLine < 0 || realImpactLine >= COLS)
                return;
        } else {
            realImpactLine = impactLine - 5;
            if (realImpactLine < 0 || realImpactLine >= ROWS)
                return;
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

    /**
     * Handles the impact of a meteor on the ship depending on its size, direction, and whether it is
     * shielded or destroyed by a cannon. The method evaluates the position of the meteor's impact,
     * checks for active shields or cannons, and determines whether the meteor causes damage to the ship.
     *
     * @param meteor The Meteor object representing the meteor that is approaching.
     *               Contains properties such as size and direction.
     * @param impactLine The line (row or column) on the ship where the meteor is expected to impact.
     *                   Its interpretation depends on the direction of the meteor (vertical or horizontal).
     */
    public void handleMeteor(Meteor meteor, int impactLine) {

        //prima di tutto converto la impactLine per far sì che rientri nei limiti della mia matrice
        int realImpactLine;
        if (meteor.getDirection() == Direction.UP || meteor.getDirection() == Direction.DOWN) {
            realImpactLine = impactLine - 4;
            if (realImpactLine < 0 || realImpactLine >= COLS)
                return;
        } else {
            realImpactLine = impactLine - 5;
            if (realImpactLine < 0 || realImpactLine >= ROWS)
                return;
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
            // in base al livello posso sparare ai lati
            boolean canShootToSide = UsersConnected.getInstance().getGameFromUsername(username).getLevel() == 2;

            // Scorro tutti i cannoni installati sulla nave
            for (Cannon s : cannons) {
                // Determino se il cannone si trova in una posizione adatta a colpire la meteora
                boolean matchesPosition = switch (meteor.getDirection()) {
                    case UP -> s.getY() == realImpactLine;
                    case DOWN -> false;
                    case RIGHT, LEFT -> canShootToSide &&
                                    (s.getX() == realImpactLine ||
                                    s.getX() == realImpactLine + 1 ||
                                    s.getX() == realImpactLine - 1);
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
     * Loads the specified item into a container located at the given coordinates
     * on the ship. The method verifies that the coordinates are valid and point
     * to a container. If the coordinates are invalid, do not point to a container,
     * or if the container cannot accept the item, an exception is thrown.
     *
     * @param item the item to be loaded into the container
     * @param i the row index of the container's location
     * @param j the column index of the container's location
     * @throws InvalidCoordinatesException if the coordinates are invalid or do
     *         not point to a tile on the ship
     * @throws ComponentMismatchException if the container at the specified
     *         coordinates is not found in the containers list
     * @throws TypeMismatchException if the component at the specified coordinates
     *         is not a container
     * @throws ContainerException if the item cannot be loaded into the container
     */
    public void loadGood(Item item, int i, int j) {
        if (!isValid(i, j) || isFree(i,j)) {
            throw new InvalidCoordinatesException("Coordinates(" + i + "," + j + ") cannon contain a tile or don't contain one");
        }
        Component tile = ship[i][j];
        switch (tile) {
            case Container container -> {
                int index = containers.indexOf(container);
                if (index == -1) {
                    throw new ComponentMismatchException("Container not found in 'containers' list: error in loadGoods of Board");
                }
                try {
                    // loadItem controlla anche se l'item può essere caricato in quello specifico container
                    containers.get(index).loadItem(item);
                } catch (ContainerException c) {
                    // Rilancio una ContainerException con maggior contesto, da gestire poi nel Controller
                    throw new ContainerException("Item cannot be loaded in container at [" + i + "][" + j + "]: " + c.getMessage());
                }
            }
            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not a container");
        }
    }


    /**
     * Updates the allowed aliens for each HousingUnit by checking for adjacent AlienAddOns.
     * For each HousingUnit, this method iterates through all available AlienAddOns and checks
     * if any of them are adjacent to the current HousingUnit. If an adjacent AlienAddOn is found,
     * its associated color is added to the list of connected addons for that specific HousingUnit.
     *
     * The adjacency is determined based on the AlienAddOn being either directly horizontal or vertical
     * to the position of the HousingUnit (not diagonal). Multiple adjacent AlienAddOns can be added
     * for a single HousingUnit.
     */
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


    /**
     * Reduces the number of batteries in a BatteryHub at the specified coordinates.
     *
     * @param i the row index of the tile on the ship
     * @param j the column index of the tile on the ship
     * @param num the number of batteries to be removed
     * @throws InvalidCoordinatesException if the coordinates do not contain a valid tile or no tile exists
     * @throws ComponentMismatchException if no matching BatteryHub is found at the specified coordinates
     * @throws BatteryOperationException if an error occurs while removing batteries
     * @throws TypeMismatchException if the component at the given coordinates is not a BatteryHub
     */
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


    /**
     * Reduces the number of crew members in a specified housing unit of the ship.
     * This method validates the provided coordinates and checks for the presence
     * of a HousingUnit tile at the specified location. If the tile exists and is
     * valid, it attempts to reduce the crew count by the specified number.
     *
     * @param i the row index of the tile to target
     * @param j the column index of the tile to target
     * @param num the number of crew members to remove from the housing unit
     * @throws InvalidCoordinatesException if the coordinates are invalid or the tile is empty
     * @throws ComponentMismatchException if the specified HousingUnit is not found in the housing unit list
     * @throws CrewOperationException if the operation to remove crew members fails
     * @throws TypeMismatchException if the tile is not a HousingUnit
     */
    public void reduceCrew(int i, int j, int num) {
        if ((!isValid(i, j)) || isFree(i,j))
            throw new InvalidCoordinatesException("Coordinates("+i+","+j+") cannon contain a tile or don't contain one");

        Component tile = ship[i][j];
        switch (tile) {
            case HousingUnit cabin -> {
                int index = housingUnits.indexOf(cabin);
                if (index == -1) {
                    throw new ComponentMismatchException("HousingUnit not found in 'housingUnit' list: error in reduceCrew of Board");
                } else {
                    try {
                        // controllo rimozione implementato in reduceOccupants
                        housingUnits.get(index).reduceOccupants(num);
                    }
                    catch (IllegalArgumentException e) {
                        throw new CrewOperationException("Failed to remove "+ num + "crew members from HousingUnit at Ship["+i+"]["+j+"]" + e.getMessage());
                    }
                }
            }
            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not a housing unit");
        }
    }

    /**
     * Calculates the number of exposed connectors on the border of the ship.
     * The method iterates through each side of the ship (up, down, left, right),
     * examines the edge components, and counts any exposed connectors.
     * A connector is considered exposed if it belongs to one of the following types:
     * SINGLE_CONNECTOR, DOUBLE_CONNECTOR, SHIELD_SINGLE_CONNECTOR, SHIELD_DOUBLE_CONNECTOR, or UNIVERSAL_CONNECTOR.
     *
     * @return the total count of exposed connectors on the ship's border.
     */
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
                    if (ship[i][j].getUp().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getUp().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getUp().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getUp().equals(Side.SHIELD_DOUBLE_CONNECTOR) || ship[i][j].getUp().equals(Side.UNIVERSAL_CONNECTOR)) {
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
                    if (ship[i][j].getDown().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getDown().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getDown().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getDown().equals(Side.SHIELD_DOUBLE_CONNECTOR) || ship[i][j].getDown().equals(Side.UNIVERSAL_CONNECTOR)) {
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
                    if (ship[i][j].getLeft().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getLeft().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getLeft().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getLeft().equals(Side.SHIELD_DOUBLE_CONNECTOR) || ship[i][j].getLeft().equals(Side.UNIVERSAL_CONNECTOR)) {
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
                    if (ship[i][j].getRight().equals(Side.SINGLE_CONNECTOR) || ship[i][j].getRight().equals(Side.DOUBLE_CONNECTOR) || ship[i][j].getRight().equals(Side.SHIELD_SINGLE_CONNECTOR) || ship[i][j].getRight().equals(Side.SHIELD_DOUBLE_CONNECTOR) || ship[i][j].getRight().equals(Side.UNIVERSAL_CONNECTOR)) {
                        count++;
                    }
                    break;
                }
            }
        }
        return count;
    }

    /**
     * Activates a cannon located at specified coordinates on the ship.
     * This method ensures the specified coordinates contain a valid
     * double cannon and activates it. Throws exceptions if the input
     * coordinates are invalid, the component is not a cannon, or the
     * cannon does not meet the required conditions.
     *
     * @param i the row index of the cannon on the ship to be activated
     * @param j the column index of the cannon on the ship to be activated
     * @throws InvalidCoordinatesException if the specified coordinates do not contain a tile
     * @throws ComponentMismatchException if the cannon at the specified coordinates is not in the list of cannons
     * @throws InvalidComponentActionException if the cannon at the specified coordinates is not a double cannon
     * @throws TypeMismatchException if the component at the specified coordinates is not a cannon
     */
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

    /**
     * Activates the shield at the specified coordinates on the ship. If the shield
     * is not present or the specified component is not a shield, an exception is thrown.
     *
     * @param i the row index of the shield on the ship
     * @param j the column index of the shield on the ship
     * @throws InvalidCoordinatesException if the specified coordinates are invalid
     *         or do not contain a tile
     * @throws ComponentMismatchException if the shield at the given coordinates
     *         is not found in the shields list
     * @throws TypeMismatchException if the component at the specified coordinates
     *         is not a shield
     */
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

    /**
     * Activates a specific engine in the ship, provided its coordinates are valid and
     * it meets the necessary conditions for activation.
     *
     * @param i the row index of the engine's location on the ship
     * @param j the column index of the engine's location on the ship
     * @throws InvalidCoordinatesException if the provided coordinates are invalid or the location is free
     * @throws ComponentMismatchException if the engine at the specified coordinates is not found in the engines list
     * @throws InvalidComponentActionException if the engine at the specified coordinates is not a double engine
     * @throws TypeMismatchException if the component at the specified coordinates is not an engine
     */
    public void activeEngine(int i, int j) {
        if ((!isValid(i, j)) || isFree(i,j))
            throw new InvalidCoordinatesException("Ship[" + i + "][" + j + "] does not contain a valid Engine.");

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

    /**
     * Calculates the total cannon strength based on the current state of the cannons and any relevant modifiers.
     * Single forward-facing cannons contribute +1 to the strength, while non-forward-facing single cannons contribute +0.5.
     * Double cannons, when activated, contribute +2 if forward-facing or +1 if not.
     * Activations for double cannons are consumed and cannot be reused.
     * If a purple alien is present in the housing units, it adds +2 to the strength only if the calculated strength is already greater than 0.
     *
     * @return the total power-strength of the ship.
     */
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

    /**
     * Calculates the engine strength based on the status and type of engines and the presence
     * of specific conditions such as a brown alien in the housing units.
     *
     * The calculation considers:
     * - Single engines contribute +1 each.
     * - Double engines contribute +2 if they are activated (and are then deactivated after usage).
     * - A brown alien in a housing unit contributes +2 if the engine strength is already greater than 0.
     *
     * @return the total engine strength as an integer value.
     */
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

    /**
     * Calculates the total number of batteries available across all battery hubs.
     *
     * @return the total number of batteries available
     */
    public int calculateBatteriesAvailable() {
        int numbatteries = 0;
        for (BatteryHub BatteryHub : batteryHubs) {
            numbatteries += BatteryHub.getNumBatteries();
        }
        return numbatteries;
    }

    /**
     * Calculates the total number of goods stored within all containers.
     * Iterates through the list of containers and sums up the number of items
     * in each container.
     *
     * @return the total count of goods across all containers
     */
    public int calculateGoods() {
        int numGoods = 0;
        for (Container c : containers) {
            numGoods += c.getItems().size();
        }
        return numGoods;
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
     * Calculates the total sales value of goods based on their color coding.
     * Each item's color contributes a specific value to the total based on the predefined rules:
     * Red contributes 4, Yellow contributes 3, Green contributes 2, and Blue contributes 1.
     *
     * @return the total monetary value of the goods sales as an integer
     */
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

    /**
     * Checks if the player has at least one welded piece on the ship grid.
     *
     * This method iterates through the ship grid, excluding the central cabin,
     * and determines if any cell contains a non-null value, which indicates a welded piece.
     *
     * @return true if the ship grid contains at least one welded piece outside the central cabin,
     *         false otherwise.
     */
    public boolean isWelded(){
        for(int i = 0; i < ROWS ; i++){
            for(int j = 0; j < COLS ; j++){
                if(!(i == 2 && j == 3)) {
                    if (ship[i][j] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getGarbage() {
        return garbage;
    }

    public void setGarbage(int offset) {
        garbage += offset;
    }

    /** Di seguito metto metodi necessari per il testing (BoardTest) */
    public List<Cannon> getCannons() {
        // ritorno copia immutabile così è solo read-only ed evito side-effects esterni
        return List.copyOf(cannons);
    }

    public List<Shield> getShields() {
        // ritorno copia immutabile così è solo read-only ed evito side-effects esterni
        return List.copyOf(shields);
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

    /**
     * Removes an item from a container located at the specified position on the ship.
     *
     * @param i the row index of the container on the ship
     * @param j the column index of the container on the ship
     * @param index the 1-based index of the item to remove from the container
     * @throws ContainerException if the specified position does not contain a valid container or if a removal action fails
     * @throws ComponentMismatchException if the specified component is not a valid container or if the index is invalid
     * @throws InvalidActionException if the specified index exceeds the number of items in the container
     * @throws TypeMismatchException if the component at the specified position is not a container
     */
    public void removeGood(int i, int j, int index){
        if(!isValid(i,j)){
            throw new ContainerException("Not a valid position for a component");
        }
        Component tile = ship[i][j];

        switch (tile) {
            case Container c -> {
                if (index <= 0) {
                    throw new ComponentMismatchException("Invalid index of item. Please insert a positive index");
                }
                // Trovo l'indice del container corrispondente a ship[i][j] nella lista dei container
                // L'oggetto in ship[i][j] è lo stesso oggetto (stesso riferimento) inserito in containers, quindi indexOf funziona correttamente.
                int container = containers.indexOf(ship[i][j]);
                // Controllo che l'indice sia valido: se è -1, significa che ship[i][j] non è un container noto
                if (container == -1) {
                    throw new ComponentMismatchException("Invalid coordinates: ship[i][j] does not contain a container.");
                }
                if (index > containers.get(container).getItems().size())
                    throw new InvalidActionException("L'indice richiesto non matcha con la quantità di merci nella stiva");

                Item itemToRemove = containers.get(container).getItems().get(index - 1);

                // provo a rimuovere item: se loseItem lancia eccezione, la raccolgo e la rilancio con contesto affinchè venga gestita meglio dal controller
                try {
                    containers.get(container).loseItem(itemToRemove);
                }
                catch (ContainerException e) {
                    throw new ContainerException("Cannon remove precious item in Container at Ship["+i+"]["+j+"]:" + e.getMessage());
                }
            }
            default -> throw new TypeMismatchException("Component at ["+i+"]["+j+"] is not a container");
        }
    }
}
