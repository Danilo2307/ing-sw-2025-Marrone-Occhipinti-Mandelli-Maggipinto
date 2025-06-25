package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.controller.Controller;
import it.polimi.ingsw.psp23.exceptions.LobbyUnavailableException;
import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.exceptions.PlayerNotExistsException;
import it.polimi.ingsw.psp23.exceptions.TuiInputException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.*;
import it.polimi.ingsw.psp23.network.rmi.ClientRMI;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.request.*;
import it.polimi.ingsw.psp23.protocol.response.*;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

/** Flusso generale dell'app: loop principale per input, mapping comandi utente -> chiamata a metodo ClientController,
 *  cambio stato. */
public class TuiApplication implements ViewAPI {
    private Client client;
    private int lastUncoveredVersion;
    private final IOManager io;
    private TuiState currentTuiState;
    private int level;

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

        /*try {
            socket.setSoTimeout(1000);
            Message messaggio = client.readMessage();
            messaggio.call(new GetEventVisitor()).call(new HandleEventVisitor(), this);

            int livello = scanner.nextInt();
            scanner.nextLine();
            messaggio = new LevelSelectionMessage(livello);
            client.sendMessage(messaggio);

            client.avvia();
            socket.setSoTimeout(0);
        } catch (SocketTimeoutException ste) {
            try {
                socket.setSoTimeout(0);
                client.avvia();
            }
            catch (SocketException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SocketException e) {
            throw new RuntimeException(e);
        }*/

        System.out.println("Welcome to GALAXY TRUCKER! Inserisci il tuo username: ");
        String username = scanner.nextLine();
        client.setUsername(username);
        // setClient(client);
        runGame();
    }

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

    /** ciclo infinito che rimane in ascolto degli input dell'utente */
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

    private static final Map<TuiState, Set<String>> aliasMap = Map.of(
            TuiState.PRELOBBY, Set.of("pesca", "scoperte", "salda", "prendi", "rilascia", "ruota", "prenota", "rimuovi", "gira", "mostra", "info", "attiva", "equipaggio", "abbandona", "", " ", "  "),
            // "accetta" in LOBBY sarà permesso solo per il primo giocatore
            TuiState.LOBBY, Set.of("accetta"),
            TuiState.BUILDING, Set.of("pesca", "scoperte", "salda", "prendi", "rilascia", "ruota", "prenota", "gira", "mostra", "info", "mazzetto", "posiziona", "scarta","abbandona"),
            TuiState.CHECK, Set.of("rimuovi", "mostra", "info", "corretta", "rotta", "abbandona"),
            TuiState.ADDCREW, Set.of("info", "mostra", "equipaggio", "finito", "rotta", "abbandona"),
            TuiState.NOTYOURTURN, Set.of("abbandona"),
            // TODO: Questi ultimi due stati sono da completare
            TuiState.PLAY, Set.of("mostra", "info", "rotta", "attiva", "rimuovi", "atterra", "pronto", "attracca", "carica", "compra", "passa","aiuto", "perdi", "sposta", "abbandona", "carta"),
            TuiState.ENDGAME, Set.of("abbandona")
    );

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

    /** command è input utente: in base a questo creo evento e il ClientController lo manda al server*/
    public void executeCommand(String command) throws RemoteException {
        String[] words = command.split(" ");

        String keyword = words[0];

        if(!isCommandLegal(keyword)){
            throw new TuiInputException("Comando non valido. Sei nello stato di " + currentTuiState + " inserisci uno dei comandi tra: " + aliasMap.get(currentTuiState));
        }

        // Questo serve nel caso in cui noi dobbiamo gestire username sbagliati, per far sì che ogni parola da noi inserita
        // durante lo stato di prelobby sia letta come uno username. Tramite la return siamo sicuri di non entrare nello
        // switch
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
                if (words.length <= 1 || words.length > 3) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else{
                if (words[1].equals("mucchio")) {
                    client.sendAction(new DrawFromHeap());
                }
                else if (words[1].equals("scoperta")) {
                    int index = Integer.parseInt(words[2]);
                    client.sendAction(new DrawFromFaceUp(index - 1, lastUncoveredVersion));
                }
                else
                    throw new TuiInputException("Comando non valido");
                }
            }
            case "scoperte" -> {
                client.sendAction(new RequestUncovered());
            }
            case "salda" -> {
                // TODO: inserire controlli interi ovunque e rendere gli indici delle tiles nella ship 1-based
                try {
                    if (words.length != 3) {
                        io.error("Non hai inviato il numero corretto di parametri, riprova");
                    }
                    else {
                        int x = Integer.parseInt(words[1]);
                        int y = Integer.parseInt(words[2]);
                        client.sendAction(new AddTile(x, y));
                    }
                }
                catch (NumberFormatException e) {
                    throw new TuiInputException("Devi inserire 2 interi!");
                }
            }
            case "prendi" -> {
                // prendi prenotata x
                if (words.length != 3) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    int index = Integer.parseInt(words[2]);
                    client.sendAction(new TakeReservedTile(index - 1));
                }
            }
            case "scarta" -> {
                client.sendAction(new ReleaseTile());
            }
            case "ruota" -> {
                client.sendAction(new RotateTile());
            }
            case "prenota" -> {
                client.sendAction(new ReserveTile());
            }
            case "rimuovi" -> {
                if (words.length > 5) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }else {
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
                    }else if (words[1].equals("batterie")) {
                        if (words.length != 5) {
                            io.error("Non hai inviato il numero corretto di parametri, riprova");
                        } else {
                            int cx = Integer.parseInt(words[2]);
                            int cy = Integer.parseInt(words[3]);
                            int num = Integer.parseInt(words[4]);
                            client.sendAction(new RemoveBatteries(cx, cy, num));
                        }
                    }
                    else {
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
                client.sendAction(new TurnHourglass());
            }
            case "mazzetto" -> {
                if (words.length != 2) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    int x = Integer.parseInt(words[1]);
                    if (x < 1 || x > 3)
                        throw new TuiInputException("I mazzetti visibili sono solo 3!");
                    client.sendAction(new TakeVisibleDeck(x));
                }
            }
            case "rilascia" -> {
                if (words.length != 2) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    int x = Integer.parseInt(words[1]);
                    if (x < 1 || x > 3)
                        throw new TuiInputException("I mazzetti visibili sono solo 3!");
                    client.sendAction(new ReleaseDeck(x));
                }
            }
            case "mostra" -> {
                if (words.length > 2) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    if (words.length == 1) {
                        client.sendAction(new RequestShip(client.getUsername()));
                    } else {
                        String nickname = words[1];
                        client.sendAction(new RequestShip(nickname));
                    }
                }
            }
            case "info" -> {
                if (words.length != 3) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    int x = Integer.parseInt(words[1]);
                    int y = Integer.parseInt(words[2]);
                    client.sendAction(new RequestTileInfo(x, y));
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
                if (words.length != 1) {
                    io.error("Non hai inviato il numero corretto di parametri, riprova");
                }
                else {
                    client.sendAction(new LeaveFlight());
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

    @Override
    public void showRequestNumPlayers() {
        io.print("Inserisci il numero di giocatori che faranno parte della partita:\n");
    }

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

    @Override
    public void showWrongUsername() {
        io.print("Username errato, inseriscine uno nuovo:\n");
    }

    @Override
    public void showRequestLevel() {
        io.print("Inserisci il livello di difficoltà a cui vuoi giocare (0 o 2): ");
    }

    @Override
    public void showTile(Component requested) {
        io.printInfoTile(requested);
    }

    @Override
    public void showShip(Component[][] ship, String owner) {
        io.print("Ecco la nave di " + owner + "\n");
        io.printShip(ship, level);
    }

    @Override
    public void showUncovered(ArrayList<Component> uncovered, int lastVersion) {
        setLastUncoveredVersion(lastVersion);
        for (Component component : uncovered) {
            io.print(io.getSymbol(component));
            io.print("\t");
        }
        io.print("\n");
    }

    @Override
    public void showError(String error) {
        io.error(error);
    }

    @Override
    public void showMessage(String message) {
        io.print(message);
    }

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

    @Override
    public void showTurn(String player) {
        io.print("Turno di " + player + " iniziato");
    }

    @Override
    public void showStart() {
        io.print("Partita iniziata");
    }

    @Override
    public void showIllegalTruck() {
        io.error("La tua nave non rispetta i criteri di Galaxy Trucker, sistemala e poi digita 'corretta'\n");
    }

    @Override
    public void showPlayerLanded(String username, int index){
        io.print("Il giocatore " + username + " e' atterrato sul pianeta numero " + (index + 1));
    }

    @Override
    public void showTimeExpired() {
        io.print("Tempo scaduto");
    }

    @Override
    public void showEndTurn(String username) {
        io.print("Turno di " + username + " finito");
    }

    @Override
    public void showEnd() {
        io.print("Partita terminata");
    }

    @Override
    public void endMatch(String message) {
        getClient().stopListeningForServerThread();
        io.print(message);
    }

    @Override
    public void showCardUpdate(String message) {
        io.print(message);
    }

    @Override
    public void showMeteor(Meteor meteor) {
        io.print("Sta arrivando una meteora " + meteor.isBig() + " dalla direzione " + meteor.getDirection());
    }

    @Override
    public void showCannonShot(int coord, CannonShot cannonShot) {
        io.print("Sta arrivando una cannonata " + cannonShot.isBig() + " dalla direzione " + cannonShot.getDirection() + coord);
    }

    @Override
    public void showDeck(ArrayList<Integer> idCards, String description) {
        io.print(description);
    }

    @Override
    public void incorrectTile(){}

    @Override
    public void showNewCard(int id, String description) {
        io.print(description);
    }

    @Override
    public void showFlightBoard(Map<Color, Integer> flightMap) {}

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

    @Override
    public void savePlayersNames(List<String> players) {
        io.print("I giocatori in partita sono:\n");
        for (String player : players) {
            io.print(player + "\n");
        }
    }


}
