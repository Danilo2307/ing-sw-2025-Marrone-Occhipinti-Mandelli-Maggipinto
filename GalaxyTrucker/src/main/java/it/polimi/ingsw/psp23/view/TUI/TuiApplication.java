package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.exceptions.TuiInputException;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.network.messages.ActionMessage;
import it.polimi.ingsw.psp23.network.socket.Client;
import it.polimi.ingsw.psp23.protocol.request.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** Flusso generale dell'app: loop principale per input, mapping comandi utente -> chiamata a metodo ClientController,
 *  cambio stato. */
public class TuiApplication {
    private Client client;
    // private ClientController cc;  ora useless perchè sendAction è in questa classe
    private int lastUncoveredVersion;
    private final IOManager io;
    private TuiState currentTuiState;

    /// TODO: capire costruttore (client=null)
    public TuiApplication() {
        io = new IOManager();
        lastUncoveredVersion = 0;
        currentTuiState = TuiState.PRELOBBY;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public IOManager getIOManager() {
        return io;
    }

    /** ciclo infinito che rimane in ascolto degli input dell'utente */
    public void runGame() {
        while (true) {
            try {
                String command = io.read();
                executeCommand(command);
            }
            catch (TuiInputException e) {
                io.error(e.getMessage());
            }
        }
    }

    public void sendAction(Action action) {
        ActionMessage actionMessage = new ActionMessage(action);
        if (client != null) {
            client.sendMessage(actionMessage);
        }
    }

    public void setLastUncoveredVersion(int lastUncoveredVersion) {
        this.lastUncoveredVersion = lastUncoveredVersion;
    }

    private static final Map<TuiState, Set<String>> aliasMap = Map.of(
            TuiState.PRELOBBY, Set.of("pesca", "scoperte", "salda", "prendi", "rilascia", "ruota", "prenota", "rimuovi", "gira", "mostra", "info", "attiva", "equipaggio"),
            // "accetta" in LOBBY sarà permesso solo per il primo giocatore
            TuiState.LOBBY, Set.of("accetta"),
            TuiState.BUILDING, Set.of("pesca", "scoperte", "salda", "prendi", "rilascia", "ruota", "prenota", "gira", "mostra", "info", "mazzetto"),
            TuiState.CHECK, Set.of("rimuovi", "mostra", "info"),
            TuiState.ADDCREW, Set.of("info", "mostra", "equipaggio", "finito"),
            TuiState.NOTYOURTURN, Set.of(),
            // TODO: Questi ultimi due stati sono da completare
            TuiState.PLAY, Set.of("mostra", "info", "attiva", "rimuovi"),
            TuiState.ENDGAME, Set.of()
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
    public void executeCommand(String command) {
        String[] words = command.split(" ");

        String keyword = words[0];

        if(!isCommandLegal(keyword)){
            throw new TuiInputException("Comando non valido. Sei nello stato di " + currentTuiState + " inserisci uno dei comandi tra: " + aliasMap.get(currentTuiState));
        }

        // Questo serve nel caso in cui noi dobbiamo gestire username sbagliati, per far sì che ogni parola da noi inserita
        // durante lo stato di prelobby sia letta come uno username. Tramite la return siamo sicuri di non entrare nello
        // switch
        if(currentTuiState == TuiState.PRELOBBY) {
            sendAction(new SetUsername(keyword));
            return;
        }

        switch (keyword) {
            // eventi inviati dal client controller via socket/rmi e verranno gestiti dal ServerHandlerEvent
            case "pesca" -> {
                if (words[1].equals("mucchio")) {
                    sendAction(new DrawFromHeap());
                }
                else if (words[1].equals("scoperta")) {
                    int index = Integer.parseInt(words[2]);
                    sendAction(new DrawFromFaceUp(index - 1, lastUncoveredVersion));
                }
                else
                    throw new TuiInputException("Comando non valido");
            }
            case "scoperte" -> {
                sendAction(new RequestUncovered());
            }
            case "salda" -> {
                // TODO: inserire controlli interi ovunque
                try {
                    int x = Integer.parseInt(words[1]);
                    int y = Integer.parseInt(words[2]);
                    sendAction(new AddTile(x, y));
                }
                catch (NumberFormatException e) {
                    throw new TuiInputException("Devi inserire 2 interi!");
                }
            }
            case "prendi" -> {
                // prendi prenotata x
                int index = Integer.parseInt(words[2]);
                sendAction(new TakeReservedTile(index - 1));
            }
            case "scarta" -> {
                sendAction(new ReleaseTile());
            }
            case "ruota" -> {
                sendAction(new RotateTile());
            }
            case "prenota" -> {
                sendAction(new ReserveTile());
            }
            case "rimuovi" -> {
                if (words[1].equals("equipaggio")) {
                    int hx = Integer.parseInt(words[2]);
                    int hy = Integer.parseInt(words[3]);
                    int num = Integer.parseInt(words[4]);
                    sendAction(new ReduceCrew(hx, hy, num));
                }
                else if(words[1].equals("merce")){
                    int cx = Integer.parseInt(words[2]);
                    int cy = Integer.parseInt(words[3]);
                    int num = Integer.parseInt(words[4]);
                    sendAction(new RemovePreciousItem(cx, cy, num));
                }
                else{
                    int x = Integer.parseInt(words[1]);
                    int y = Integer.parseInt(words[2]);
                    sendAction(new RemoveTile(x, y));
                }
            }
            case "gira" -> {
                sendAction(new TurnHourglass());
            }
            case "mazzetto" -> {
                int x = Integer.parseInt(words[1]);
                if (x<1 || x>3)
                    throw new TuiInputException("I mazzetti visibili sono solo 3!");
                sendAction(new TakeVisibleDeck(x));
            }
            case "rilascia" -> {
                int x = Integer.parseInt(words[1]);
                if (x<1 || x>3)
                    throw new TuiInputException("I mazzetti visibili sono solo 3!");
                sendAction(new ReleaseDeck(x));
            }
            case "mostra" -> {
                sendAction(new RequestShip());
            }
            case "info" -> {
                int x = Integer.parseInt(words[1]);
                int y = Integer.parseInt(words[2]);
                sendAction(new RequestTileInfo(x, y));
            }
            case "attiva" -> {
                switch (words[1]) {
                    case "cannone" -> {
                        int cx = Integer.parseInt(words[2]);
                        int cy = Integer.parseInt(words[3]);
                        int bx = Integer.parseInt(words[4]);
                        int by = Integer.parseInt(words[5]);
                        sendAction(new ActivateCannon(cx, cy, bx, by));
                    }
                    case "motore" -> {
                        int ex = Integer.parseInt(words[2]);
                        int ey = Integer.parseInt(words[3]);
                        int bx = Integer.parseInt(words[4]);
                        int by = Integer.parseInt(words[5]);
                        sendAction(new ActivateEngine(ex, ey, bx, by));
                    }
                    case "scudo" -> {
                        int sx = Integer.parseInt(words[2]);
                        int sy = Integer.parseInt(words[3]);
                        int bx = Integer.parseInt(words[4]);
                        int by = Integer.parseInt(words[5]);
                        sendAction(new ActivateShield(sx, sy, bx, by));
                    }
                }
            }
            case "equipaggio" -> {
                // astronauta: equipaggio <x> <y>
                if (words.length == 3) {
                    int x = Integer.parseInt(words[1]);
                    int y = Integer.parseInt(words[2]);
                    sendAction(new SetCrew(x, y, false, null));
                }
                // alieno: equipaggio alieno <colore> <x> <y>
                else if (words.length == 5 && words[1].equalsIgnoreCase("alieno")) {
                    Color color = Color.parse(words[2]);
                    if (color == null || !(color.equals(Color.Brown) || color.equals(Color.Purple)))
                        throw new TuiInputException("Alieno può essere solo viola o marrone!");
                    int x = Integer.parseInt(words[3]);
                    int y = Integer.parseInt(words[4]);
                    sendAction(new SetCrew(x, y, true, color));
                }
                else {
                    throw new TuiInputException(
                            "Uso corretto:\n" +
                                    "  equipaggio <x> <y>  -> aggiungi i 2 astronauti\n" +
                                    "  equipaggio alieno <colore> <x> <y> -> aggiungi alieno"
                    );
                }
            }
            case "accetta" -> {
                int number;
                number = Integer.parseInt(words[1]);
                boolean error = false;
                while (number < 2 || number > 4 || error) {

                    io.error("Il numero di giocatori deve essere compreso  tra 2 e 4, inserisci un numero valido!\n");

                    try {
                        number = Integer.parseInt(io.read());
                        error = false;
                    }
                    catch (NumberFormatException e) {
                        error = true;
                    }

                }
                sendAction(new RegisterNumPlayers(number));
            }

            case "passa" -> {
                sendAction(new NextTurn());
            }
            case "compra" -> {
                sendAction(new BuyShip());
            }
            case "aiuto" -> {
                sendAction(new Help());
            }
            case "attracca" -> {
                sendAction(new DockStation());
            }
            case "carica" -> {
                int lx = Integer.parseInt(words[1]);
                int ly = Integer.parseInt(words[2]);
                sendAction(new LoadGood(lx, ly));
            }
            case "pronto" -> {
                sendAction(new Ready());
            }
            case "atterra" -> {
                int pi = Integer.parseInt(words[1]);
                sendAction(new Land(pi));
            }
            case "finito" -> {
                sendAction(new Finished());
            }
            default -> throw new TuiInputException("Comando sconosciuto");
        }
    }
}
