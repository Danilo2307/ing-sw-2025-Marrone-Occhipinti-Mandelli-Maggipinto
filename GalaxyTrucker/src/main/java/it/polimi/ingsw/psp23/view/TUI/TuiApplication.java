package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.exceptions.TuiInputException;
import it.polimi.ingsw.psp23.model.helpers.CannonShot;
import it.polimi.ingsw.psp23.model.helpers.Meteor;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.messages.*;
import it.polimi.ingsw.psp23.protocol.request.*;
import it.polimi.ingsw.psp23.protocol.response.*;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.*;


public class TuiApplication implements ViewAPI {
    private Client client;
    private int lastUncoveredVersion;
    private final IOManager io;
    private TuiState currentTuiState;
    private int level;
    private boolean firstHourGlassExpired = false;
    private boolean secondHourGlassExpired = false;

    public TuiApplication() {
        io = new IOManager();
        lastUncoveredVersion = 0;
        currentTuiState = TuiState.PRELOBBY;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    public IOManager getIOManager() {
        return io;
    }

    /**
     * Initializes the client's setup phase for the TUI application and handles the initial interaction flow.
     *
     * This method establishes a connection with the client, processes incoming events, and allows the user
     * to perform initial actions such as selecting a match or creating a new game level. It also assigns a
     * username to the client and transitions to the main game loop.
     *
     * It uses a loop to handle user input and ensure valid match selection, with retry logic implemented
     * in case the match is not available or certain constraints are not met. Once a valid action is confirmed,
     * it proceeds to either set up a custom game or join an existing game based on the user's input.
     *
     * Moreover, the method handles communication with the server using socket operations and message passing,
     * leveraging a visitor pattern for processing event and message types.
     *
     * @throws RemoteException if any remote communication error occurs during setup
     * @throws RuntimeException if a socket exception occurs while modifying socket timeout settings
     */
    @Override
    public void setup() throws RemoteException {
        Socket socket = client.getSocket();
        Scanner scanner = new Scanner(System.in);
        try {
            Event e = client.readMessage().call(new GetEventVisitor());
            e.call(new HandleEventVisitor(), this);
        }catch (SocketTimeoutException e){

        }

        int scelta = -1;

        boolean error = false;
        do {
            try {
                scelta = scanner.nextInt();
                scanner.nextLine();
                client.sendAction(new UserDecision(scelta));
                socket.setSoTimeout(100);
                client.readMessage();
                error = true;
                io.error("Non puoi partecipare a questo match, inserisci un altro id");
            }catch (SocketTimeoutException e){
                error = false;
                try{
                    socket.setSoTimeout(0);
                }catch (SocketException se){
                    throw new RuntimeException(se);
                }
            }
            catch(SocketException s){
                throw new RuntimeException();
            }
        }while(error);

        if(scelta == 0){
            showRequestLevel();
            int livello = scanner.nextInt();
            scanner.nextLine();
            Message messaggio = new LevelSelectionMessage(livello);
            client.sendMessage(messaggio);

            client.avvia();
        }

        else{
            client.avvia();
            client.setId(scelta-1);
        }


        System.out.println("Welcome to GALAXY TRUCKER! Inserisci il tuo username: ");
        String username = scanner.nextLine();
        client.setUsername(username);
        // setClient(client);
        runGame();
    }

    /**
     * Sets up the Remote Method Invocation (RMI) for initializing a connection between the client
     * and the game server, and manages the client's interaction during the setup phase.
     *
     * The method facilitates tasks such as displaying and selecting available matches, creating new games,
     * assigning a username, setting game levels, and determining the number of players in a new game. It also
     * conducts error checks for invalid input scenarios, ensuring proper communication with the game server.
     *
     * @param nameConnection the identifier associated with the connection being established for the user
     * @throws RemoteException if any remote communication error occurs
     */
    public void setupRMI(String nameConnection) throws RemoteException {

        Scanner scanner = new Scanner(System.in);

        List<List<Integer>> matchesAvailable = client.getGameServer().getGamesAvailables();
        client.getGameServer().sendToUser(nameConnection, new DirectMessage(new LobbyAvailable(matchesAvailable)));

        int scelta = 0;
        List<Integer> matchIndexesAvailable = matchesAvailable.stream().mapToInt(l -> l.get(0)).boxed().toList();
        boolean error = false;
        do {
            scelta = scanner.nextInt();
            scanner.nextLine();
            if(scelta != 0 && (!matchIndexesAvailable.contains(scelta - 1) || client.getGameServer().getGameStatus(scelta - 1 ) != GameStatus.Setup)){
                error = true;
                io.error("Non puoi partecipare a questo match, inserisci un altro id");
            }
            else{
                error = false;
            }
        }while (error);

        // client.sendAction(new UserDecision(scelta));

        if(scelta == 0) {

            Message msg = (new DirectMessage(new SelectLevel()));
            client.getGameServer().sendToUser(nameConnection, msg);

            scanner = new Scanner(System.in);
            int level = scanner.nextInt();
            scanner.nextLine();

            client.getGameServer().setGameLevel(level);
        }

        io.print("Welcome to GALAXY TRUCKER! Inserisci il tuo username: ");

        String username = null;

        error = false;
        do {
            try {
                scanner = new Scanner(System.in);
                username = scanner.nextLine();

                int gameIdConsidering = scelta; // 1-based
                if(scelta == 0){
                    gameIdConsidering = client.getGameServer().getGamesSize();
                }
                client.getGameServer().setPlayerUsername(username,  gameIdConsidering - 1);
                client.setId(gameIdConsidering - 1);
                error = false;
                io.print("Username settato correttamente\n");
                client.setUsername(username);
                if(scelta == 0){
                    io.print("Inserisci il numero di giocatori presenti nella partita (minimo 2 massimo 4): ");
                    int avversari;
                    do {
                        avversari = scanner.nextInt();
                        if(avversari <= 1 || avversari > 4) {
                            io.error("Inserisci un numero valido di players");
                        }
                        scanner.nextLine();
                    }while(avversari <= 1 || avversari > 4);
                    client.getGameServer().setNumRequestedPlayers(avversari, username);
//                    client.open();
                }
            } catch (PlayerExistsException e) {
                io.error("Questo username è già in uso, scegline un altro!!");
                error = true;
            }
        } while (error);
        client.getGameServer().sendToUser(nameConnection, new DirectMessage(new AppropriateUsername(username, client.getGameServer().getGameLevel(client.getId()))));
        if(client.getGameServer().getNumPlayersConnected(client.getId()) == client.getGameServer().getNumRequestedPlayers(client.getId())){
            client.getGameServer().startBuildingPhase(client.getId());
        }
        runGame();
    }

    /**
     * Executes the main loop of the text-based user interface (TUI) application.
     *
     * This method runs continuously in a loop, awaiting user input through the `io.read()` method.
     * Each input command is processed using the `executeCommand` method. It handles both standard
     * user input errors and exceptions related to remote method invocation (RMI) errors.
     *
     * This loop runs indefinitely unless interrupted or terminated externally.
     */
    public void runGame() {
        while (true) {
            try {
                String command = io.read();
                try {
                    executeCommand(command);
                }catch (RemoteException e) {
                    e.getStackTrace();
                }
            }
            catch (TuiInputException e) {
                io.error(e.getMessage());
            }
        }
    }


    public void setLastUncoveredVersion(int lastUncoveredVersion) {
        this.lastUncoveredVersion = lastUncoveredVersion;
    }

    /**
     * A mapping that associates specific commands (represented as sets of strings)
     * to different states of the Text User Interface (TUI).
     *
     * This map defines which commands are valid for a given state of the TUI during the game lifecycle.
     * Each key in the map corresponds to a particular `TuiState`, representing a phase or context of the application,
     * and the associated value is a set of commands that are allowed in that state.
     * The commands are used to interact with the game, perform actions, or update state.
     *
     * Key points:
     * - PRELOBBY includes initial setup commands.
     * - LOBBY allows only the "accetta" command for the first player in the game.
     * - BUILDING involves commands for building actions such as drawing, positioning, rotating, and managing components.
     * - CHECK provides commands for validation, corrections, and showing information.
     * - ADDCREW is for placing the crew (astronauts or alien).
     * - NOTYOURTURN permits `abbandona` as an exit command for scenarios when it's not the user's turn.
     * - PLAY consists of commands for the main gameplay actions, such as moving, activating abilities, and passing turns.
     * - ENDGAME restricts to commands relevant to the game's conclusion phase such as exiting.
     */
    private static final Map<TuiState, Set<String>> aliasMap = Map.of(
            TuiState.PRELOBBY, Set.of("pesca", "scoperte", "salda", "prendi", "rilascia", "ruota", "prenota", "rimuovi", "gira", "mostra", "info", "attiva", "equipaggio", "", " ", "  "),
            // "accetta" in LOBBY sarà permesso solo per il primo giocatore
            TuiState.LOBBY, Set.of("accetta"),
            TuiState.BUILDING, Set.of("pesca", "scoperte", "salda", "prendi", "rilascia", "ruota", "prenota", "gira", "mostra", "info", "mazzetto", "posiziona", "scarta"),
            TuiState.CHECK, Set.of("rimuovi", "mostra", "info", "corretta", "rotta"),
            TuiState.ADDCREW, Set.of("info", "mostra", "equipaggio", "finito", "rotta", "abbandona"),
            TuiState.NOTYOURTURN, Set.of("abbandona"),
            TuiState.PLAY, Set.of("mostra", "info", "rotta", "attiva", "rimuovi", "atterra", "pronto", "attracca", "carica", "compra", "passa","aiuto", "perdi", "sposta", "abbandona", "carta", "crediti", "skippa"),
            TuiState.ENDGAME, Set.of("abbandona")
    );

    /**
     * Evaluates the legality of a command based on the current TUI (Text-Based User Interface) state.
     *
     * This method checks if the specified command (keyword) is allowed or disallowed within the context
     * of the current application state. For the PRELOBBY state, it verifies that the command does not
     * belong to a restricted list of commands. For all other states, it checks whether the command
     * exists in the set of valid commands for the current state.
     *
     * @param keyword the command keyword to evaluate for legality
     * @return true if the command is legal in the current state, false otherwise
     */
    private boolean isCommandLegal(String keyword) {
        if(currentTuiState == TuiState.PRELOBBY) {
            return !aliasMap.get(TuiState.PRELOBBY).contains(keyword);
        }
        else{
            return aliasMap.get(currentTuiState).contains(keyword);
        }
    }

    public void setState(TuiState state) {
        currentTuiState = state;
    }

    /**
     * Executes a command by interpreting the input string and triggering the
     * appropriate action based on the current TUI (Text User Interface) state.
     * This method handles user input, validates the command format, and sends
     * corresponding actions to the server depending on the command type.
     *
     * @param command the input string representing a command entered by the user
     * @throws RemoteException if a network-related exception occurs during remote method invocation
     */
    public void executeCommand(String command) throws RemoteException {
        String[] words = command.split(" ");

        String keyword = words[0];

        if(!isCommandLegal(keyword)){
            throw new TuiInputException("Comando non valido. Sei nello stato di " + currentTuiState + " inserisci uno dei comandi tra: " + aliasMap.get(currentTuiState));
        }

        // Questo serve nel caso in cui noi dobbiamo gestire username sbagliati, per far sì che ogni parola da noi inserita
        // durante lo stato di prelobby sia letta come uno username. Tramite la return siamo sicuri di non entrare nello switch
        if(currentTuiState == TuiState.PRELOBBY) {
            client.sendAction(new SetUsername(keyword));
            return;
        }

        switch (keyword) {
            // eventi inviati dal client controller via socket/rmi e verranno gestiti dal ServerHandlerEvent
            case "accetta" -> {
                if (words.length != 2) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    int number;
                    number = Integer.parseInt(words[1]);
                    boolean error = false;
                    while (number < 2 || number > 4 || error) {

                        io.error("Il numero di giocatori deve essere compreso  tra 2 e 4, inserisci un numero valido!\n");

                        try {
                            number = Integer.parseInt(io.read());
                            error = false;
                        } catch (NumberFormatException e) {
                            error = true;
                        }

                    }
                    client.sendAction(new RegisterNumPlayers(number));
                }
            }
            case "pesca" -> {
                if (!secondHourGlassExpired){
                    if (words.length <= 1 || words.length > 3) {
                        io.error("Non hai inviato il numero corretto di parametri, riprova");
                    } else {
                        if (words[1].equals("mucchio")) {
                            client.sendAction(new DrawFromHeap());
                        } else if (words[1].equals("scoperta")) {
                            int index = Integer.parseInt(words[2]);
                            client.sendAction(new DrawFromFaceUp(index - 1, lastUncoveredVersion));
                        } else
                            throw new TuiInputException("Comando non valido");
                    }
                }
            }
            case "scoperte" -> {
                if (!secondHourGlassExpired) {
                    client.sendAction(new RequestUncovered());
                }
            }
            case "salda" -> {
                if (!secondHourGlassExpired){
                    try {
                        if (words.length != 3) {
                            io.error("Non hai inviato il numero corretto di parametri, riprova");
                        } else {
                            int x = Integer.parseInt(words[1]);
                            int y = Integer.parseInt(words[2]);
                            client.sendAction(new AddTile(x, y));
                        }
                    } catch (NumberFormatException e) {
                        throw new TuiInputException("Devi inserire 2 interi!");
                    }
                }
            }
            case "prendi" -> {
                if (!secondHourGlassExpired) {
                    // prendi prenotata x
                    if (words.length != 3) {
                        io.error("Non hai inviato il numero corretto di parametri, riprova");
                    } else {
                        int index = Integer.parseInt(words[2]);
                        if (index <= 0 || index >= 3) {
                            throw new TuiInputException("Devi inserire un numero compreso tra 1 e 2!");
                        }
                        client.sendAction(new TakeReservedTile(index - 1));
                    }
                }
            }
            case "scarta" -> {
                if (!secondHourGlassExpired) {
                    client.sendAction(new ReleaseTile());
                }
            }
            case "ruota" -> {
                if (!secondHourGlassExpired) {
                    client.sendAction(new RotateTile());
                }
            }
            case "prenota" -> {
                if (!secondHourGlassExpired) {
                    client.sendAction(new ReserveTile());
                }
            }
            case "rimuovi" -> {
                if (words.length > 5) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                } else {
                    if (words[1].equals("equipaggio")) {
                        if (words.length != 5) {
                            io.error("Non hai inviato il numero corretto di parametri, riprova");
                        } else {
                            int hx = Integer.parseInt(words[2]);
                            int hy = Integer.parseInt(words[3]);
                            int num = Integer.parseInt(words[4]);
                            client.sendAction(new ReduceCrew(hx, hy, num));
                        }
                    } else if (words[1].equals("merce")) {
                        if (words.length != 5) {
                            io.error("Non hai inviato il numero corretto di parametri, riprova");
                        } else {
                            int cx = Integer.parseInt(words[2]);
                            int cy = Integer.parseInt(words[3]);
                            int num = Integer.parseInt(words[4]);
                            client.sendAction(new RemovePreciousItem(cx, cy, num));
                        }
                    } else if (words[1].equals("batterie")) {
                        if (words.length != 5) {
                            io.error("Non hai inviato il numero corretto di parametri, riprova");
                        } else {
                            int cx = Integer.parseInt(words[2]);
                            int cy = Integer.parseInt(words[3]);
                            int num = Integer.parseInt(words[4]);
                            client.sendAction(new RemoveBatteries(cx, cy, num));
                        }
                    } else {
                        if (words.length != 3) {
                            io.error("Non hai inviato il numero corretto di parametri, riprova");
                        } else {
                            int x = Integer.parseInt(words[1]);
                            int y = Integer.parseInt(words[2]);
                            client.sendAction(new RemoveTile(x, y));
                        }
                    }
                }
            }
            case "gira" -> {
                if (!secondHourGlassExpired) {
                    client.sendAction(new TurnHourglass());
                }
            }
            case "mazzetto" -> {
                if (!secondHourGlassExpired) {
                    if (words.length != 2) {
                        io.error("Non hai inviato il numero corretto di parametri, riprova");
                    } else {
                        int x = Integer.parseInt(words[1]);
                        if (x < 1 || x > 3)
                            throw new TuiInputException("I mazzetti visibili sono solo 3!");
                        client.sendAction(new TakeVisibleDeck(x));
                    }
                }
            }
            case "rilascia" -> {
                if (!secondHourGlassExpired) {
                    if (words.length != 2) {
                        io.error("Non hai inviato il numero corretto di parametri, riprova");
                    } else {
                        int x = Integer.parseInt(words[1]);
                        if (x < 1 || x > 3)
                            throw new TuiInputException("I mazzetti visibili sono solo 3!");
                        client.sendAction(new ReleaseDeck(x));
                    }
                }
            }
            case "mostra" -> {
                if (!secondHourGlassExpired) {
                    if (words.length > 2) {
                        io.error("Non hai inviato il numero corretto di parametri, riprova");
                    } else {
                        if (words.length == 1) {
                            client.sendAction(new RequestShip(client.getUsername()));
                        } else {
                            String nickname = words[1];
                            client.sendAction(new RequestShip(nickname));
                        }
                    }
                }
            }
            case "info" -> {
                if (!secondHourGlassExpired) {
                    if (words.length != 3) {
                        io.error("Non hai inviato il numero corretto di parametri, riprova");
                    } else {
                        int x = Integer.parseInt(words[1]);
                        int y = Integer.parseInt(words[2]);
                        client.sendAction(new RequestTileInfo(x, y));
                    }
                }
            }
            case "rotta" -> {
                client.sendAction(new ShowPlayersPositions());
            }
            case "attiva" -> {
                if (words.length != 6) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else{

                    switch (words[1]) {
                        case "cannone" -> {
                            int cx = Integer.parseInt(words[2]);
                            int cy = Integer.parseInt(words[3]);
                            int bx = Integer.parseInt(words[4]);
                            int by = Integer.parseInt(words[5]);
                            client.sendAction(new ActivateCannon(cx, cy, bx, by));
                        }
                        case "motore" -> {
                            int ex = Integer.parseInt(words[2]);
                            int ey = Integer.parseInt(words[3]);
                            int bx = Integer.parseInt(words[4]);
                            int by = Integer.parseInt(words[5]);
                            client.sendAction(new ActivateEngine(ex, ey, bx, by));
                        }
                        case "scudo" -> {
                            int sx = Integer.parseInt(words[2]);
                            int sy = Integer.parseInt(words[3]);
                            int bx = Integer.parseInt(words[4]);
                            int by = Integer.parseInt(words[5]);
                            client.sendAction(new ActivateShield(sx, sy, bx, by));
                        }
                    }
                }
            }
            case "equipaggio" -> {
                if (words.length != 3 && words.length != 5) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    // astronauta: equipaggio <x> <y>
                    if (words.length == 3) {
                        int x = Integer.parseInt(words[1]);
                        int y = Integer.parseInt(words[2]);
                        client.sendAction(new SetCrew(x, y, false, null));
                    }
                    // alieno: equipaggio alieno <colore> <x> <y>
                    else if (words.length == 5 && words[1].equalsIgnoreCase("alieno")) {
                        Color color = Color.parse(words[2]);
                        if (color == null || !(color.equals(Color.Brown) || color.equals(Color.Purple)))
                            throw new TuiInputException("Alieno può essere solo viola o marrone!");
                        int x = Integer.parseInt(words[3]);
                        int y = Integer.parseInt(words[4]);
                        client.sendAction(new SetCrew(x, y, true, color));
                    } else {
                        throw new TuiInputException(
                                "Uso corretto:\n" +
                                        "  equipaggio <x> <y>  -> aggiungi i 2 astronauti\n" +
                                        "  equipaggio alieno <colore> <x> <y> -> aggiungi alieno"
                        );
                    }
                }
            }
            case "passa" -> {
                client.sendAction(new NextTurn());
            }
            case "crediti" -> {
                client.sendAction(new EarnCredits());
            }
            case "skippa" -> {
                client.sendAction(new LoadGood(-1,-1));
            }
            case "compra" -> {
                client.sendAction(new BuyShip());
            }
            case "aiuto" -> {
                client.sendAction(new Help());
            }
            case "attracca" -> {
                client.sendAction(new DockStation());
            }
            case "carica" -> {
                if (words.length != 3) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    int lx = Integer.parseInt(words[1]);
                    int ly = Integer.parseInt(words[2]);
                    client.sendAction(new LoadGood(lx, ly));
                }
            }
            case "pronto" -> {
                client.sendAction(new Ready());
            }
            case "atterra" -> {
                if (words.length != 2) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    int pi = Integer.parseInt(words[1]);
                    client.sendAction(new Land(pi));
                }
            }
            case "finito" -> {
                client.sendAction(new Finished());
            }
            case "posiziona" -> {
                client.sendAction(new Put());

            }
            case "corretta" -> {
                client.sendAction(new Fixed());
            }
            case "perdi" -> {
                if (words.length != 4) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    int i = Integer.parseInt(words[1]);
                    int j = Integer.parseInt(words[2]);
                    int itemIndex = Integer.parseInt(words[3]);
                    client.sendAction(new LoseGood(i, j, itemIndex));
                }
            }
            case "sposta" -> {
                if (words.length != 6) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    int fromX = Integer.parseInt(words[1]);
                    int fromY = Integer.parseInt(words[2]);
                    int index = Integer.parseInt(words[3]);
                    int toX = Integer.parseInt(words[4]);
                    int toY = Integer.parseInt(words[5]);
                    client.sendAction(new MoveGood(fromX, fromY, index, toX, toY));
                }
            }
            case "abbandona" -> {
                if (!secondHourGlassExpired) {
                    if (words.length != 1) {
                        io.error("Non hai inviato il numero corretto di parametri, riprova");
                    } else {
                        client.sendAction(new LeaveFlight());
                    }
                }
            }
            case "carta" -> {
                client.sendAction(new DrawCard());
            }
            default -> throw new TuiInputException("Comando sconosciuto");
        }
    }

    public Client getClient() {
        return client;
    }

    /**
     * Prompts the user to enter the number of players for the match.
     */
    @Override
    public void showRequestNumPlayers() {
        io.print("Inserisci il numero di giocatori che faranno parte della partita:\n");
    }

    /**
     * This method assigns the provided username to the client if the connection is not
     * established via RMI. It also prints a welcome message, sets the game level,
     * and transitions the application state to the LOBBY state.
     *
     * @param username the username to be assigned to the client
     * @param level the initial level of the game to be set
     */
    @Override
    public void showAppropriateUsername(String username, int level) {
        try {
            if (!client.isRmi()) {
                client.getSocketHandler().setUsername(username);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        io.print("Benvenuto in Galaxy Trucker!!\n");
        this.level = level;
        setState(TuiState.LOBBY);
    }

    /**
     * Displays a message to inform the user that the entered username is invalid
     * and prompts them to input a new one.
     */
    @Override
    public void showWrongUsername() {
        io.print("Username errato, inseriscine uno nuovo:\n");
    }

    /**
     * Prompts the user to enter the desired difficulty level for gameplay.
     */
    @Override
    public void showRequestLevel() {
        io.print("Inserisci il livello di difficoltà a cui vuoi giocare (0 o 2): ");
    }

    /**
     * Displays the information of the specified tile component.
     * This method invokes the IO manager to print details of the provided tile.
     *
     * @param requested the tile component to be displayed
     */
    @Override
    public void showTile(Component requested) {
        io.printInfoTile(requested);
    }

    /**
     * Displays the ship grid for a specified owner.
     *
     * This method prints a message indicating whose ship is being displayed
     * and delegates the actual printing of the ship grid to the IO manager.
     *
     * @param ship the 2D array of Components representing the ship's structure
     * @param owner the name of the ship's owner
     */
    @Override
    public void showShip(Component[][] ship, String owner) {
        io.print("Ecco la nave di " + owner + "\n");
        io.printShip(ship, level);
    }

    /**
     * Displays a list of uncovered components along with their respective symbols
     * and updates the last uncovered version.
     *
     * @param uncovered a list of components that have been uncovered and need to be displayed
     * @param lastVersion the version identifier for the most recently uncovered components
     */
    @Override
    public void showUncovered(ArrayList<Component> uncovered, int lastVersion) {
        setLastUncoveredVersion(lastVersion);
        for (Component component : uncovered) {
            io.print(io.getSymbol(component));
            io.print("\t");
        }
        io.print("\n");
    }

    /**
     * Displays an error message to the user.
     *
     * @param error the error message to be displayed
     */
    @Override
    public void showError(String error) {
        io.error(error);
    }

    /**
     * Displays a message to the user by printing it using the IO manager.
     *
     * @param message the message to be displayed to the user
     */
    @Override
    public void showMessage(String message) {
        io.print(message);
    }

    /**
     * Handles state changes in the game by updating the current state of the TUI
     * and notifying the user about the change
     *
     * @param newState the new state of the game, represented as a GameStatus value
     */
    @Override
    public void stateChanged(GameStatus newState) {
        io.print("Stato modificato a: " + newState + "\n");
        switch (newState){
            case Building -> setState(TuiState.BUILDING);
            case CheckBoards -> setState(TuiState.CHECK);
            case SetCrew -> setState(TuiState.ADDCREW);
            case Playing -> setState(TuiState.PLAY);
        }
    }


    /**
     * Displays an error message to indicate that the player's ship does not comply
     * with the standards of Galaxy Trucker. The message instructs the user to correct
     * their ship and type "corretta" to proceed.
     */
    @Override
    public void showIllegalTruck() {
        io.error("La tua nave non rispetta i criteri di Galaxy Trucker, sistemala e poi digita 'corretta'\n");
    }

    /**
     * Displays information about an incoming cannon shot on the user interface.
     *
     * @param coord the coordinate on which the cannon shot is being targeted
     * @param cannonShot an instance of the CannonShot class containing details
     *                   about the shot, such as its size and direction
     */
    @Override
    public void showCannonShot(int coord, CannonShot cannonShot) {
        io.print("Sta arrivando una cannonata " + cannonShot.isBig() + " dalla direzione " + cannonShot.getDirection() + coord);
    }


    /**
     * Displays a message indicating that the time of the hourglass (90 seconds) has expired.
     */
    @Override
    public void showTimeExpired() {
        io.print("Tempo scaduto\n");
        if(!firstHourGlassExpired && !secondHourGlassExpired) firstHourGlassExpired = true;
        else if(firstHourGlassExpired && !secondHourGlassExpired) secondHourGlassExpired = true;
    }


    /**
     * Ends the current match and displays a concluding message to the user.
     *
     * @param message the message to be displayed to indicate the end of the match
     */
    @Override
    public void endMatch(String message) {
        io.print(message);
    }

    /**
     * Stop the current match and close view.
     *
     * @param message the message to be displayed to indicate the end of the match
     */

    @Override
    public void stopMatch(String message) {
        io.print(message);
        System.exit(130);
    }

    /**
     * Displays updates or messages specifically related to card gameflows
     *
     * @param message the message containing details about the card update
     */
    @Override
    public void showCardUpdate(String message) {
        io.print(message);
    }

    /**
     * Displays the description of the deck requested by the user.
     *
     * @param idCards a list of card identifiers that constitute the deck
     * @param description a textual description of the deck to be displayed
     */
    @Override
    public void showDeck(ArrayList<Integer> idCards, String description) {
        io.print(description);
    }

    /**
     * Notifies the user of an incorrect tile placement during the gameplay.
     */
    @Override
    public void incorrectTile(){}

    /**
     * Displays the details of a new card to the user.
     *
     * @param id  the unique identifier of the new card
     * @param description the description or details of the card to be displayed
     */
    @Override
    public void showNewCard(int id, String description) {
        io.print(description);
    }

    /**
     * Displays the flight board along with the current flight point rankings for each player.
     * It is used to keep track of the progress of different players during the game.
     *
     * @param flightMap a mapping of colors (Color) to their respective flight points (Integer)
     */
    @Override
    public void showFlightBoard(Map<Color, Integer> flightMap) {}

    /**
     * Displays a list of available game lobbies and provides the user with an option to either
     * join an existing game or create a new one.
     *
     * @param availableLobbies a list of lists where each inner list represents a lobby, containing
     *        information about the lobby such as its ID, the current number of players,
     *        the maximum number of players, and the lobby level
     */
    @Override
    public void showAvailableLobbies(List<List<Integer>> availableLobbies) {
        io.print("Scegli se partecipare ad una partita esistente o se crearne una nuova\n\t0: crea una nuova partita\n");
        for (List<Integer> list : availableLobbies) {
            if(list.get(3) == 0) {
                io.print("\t" + (list.get(0) + 1) + ": " + "volo di prova, numero di players presenti: " + list.get(1).toString() + ", numero di players massimo: " + list.get(2).toString() + "\n");
            }
            else{
                io.print("\t" + (list.get(0) + 1) + ": " + "partita di livello " + list.get(3) + ", numero di players presenti: " + list.get(1).toString() + ", numero di players massimo: " + list.get(2).toString() + "\n");
            }
        }
    }

    /**
     * Displays the final rankings by printing each entry in the provided ranking list.
     * Each entry consists of a player's name and their corresponding score.
     * Outputs a message indicating whether the player has won or lost based on their score.
     *
     * @param ranking a list of entries where each entry contains a player's name as a key
     *                and their score as a value
     */
    @Override
    public void showRanking(List<AbstractMap.SimpleEntry<String,Integer>> ranking) {
        io.print("===== CLASSIFICA FINALE =====\n");

        for (AbstractMap.SimpleEntry<String,Integer> rank : ranking) {
            String name = rank.getKey();
            int value = rank.getValue();

            io.print(name + ": " + value);
            if (value > 0) {
                io.print(" --> ha vinto!\n");
            }
            else {
                io.print(" --> ha perso!\n");
            }
        }

        io.print("=============================");
    }

    /**
     * Saves and displays the names of the players in the game.
     *
     * @param players a list of player names to be saved and displayed
     */
    @Override
    public void savePlayersNames(List<String> players) {
        io.print("I giocatori in partita sono:\n");
        for (String player : players) {
            io.print(player + "\n");
        }
    }


}
