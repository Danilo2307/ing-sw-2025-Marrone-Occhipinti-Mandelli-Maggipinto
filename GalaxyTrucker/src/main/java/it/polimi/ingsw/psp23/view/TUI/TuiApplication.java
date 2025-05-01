package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.exceptions.TuiInputException;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.network.messages.ActionMessage;
import it.polimi.ingsw.psp23.network.socket.Client;
import it.polimi.ingsw.psp23.protocol.request.*;

/** Flusso generale dell'app: loop principale per input, mapping comandi utente -> chiamata a metodo ClientController,
 *  cambio stato. */
public class TuiApplication {
    private Client client;
    // private ClientController cc;  ora useless perchè sendAction è in questa classe
    private int lastUncoveredVersion;
    private final IOManager io;

    /// TODO: capire costruttore (client=null)
    public TuiApplication() {
        io = new IOManager();
        lastUncoveredVersion = 0;
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

    /** command è input utente: in base a questo creo evento e il ClientController lo manda al server*/
    public void executeCommand(String command) {
        String[] words = command.split(" ");

        String keyword = words[0];
        switch (keyword) {
            // eventi inviati dal client controller via socket/rmi e verranno gestiti dal ServerHandlerEvent
            case "pesca" -> {
                if (words[1].equals("mucchio")) {
                    sendAction(new DrawFromHeap());
                }
                else if (words[1].equals("scoperta")) {
                    int index = Integer.parseInt(words[2]);
                    sendAction(new DrawFromFaceUp(index, lastUncoveredVersion));
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
                sendAction(new TakeReservedTile(index));
            }
            case "rilascia" -> {
                sendAction(new ReleaseTile());
            }
            case "ruota" -> {
                sendAction(new RotateTile());
            }
            case "prenota" -> {
                sendAction(new ReserveTile());
            }
            case "rimuovi" -> {
                int x = Integer.parseInt(words[1]);
                int y = Integer.parseInt(words[2]);
                sendAction(new RemoveTile(x, y));
            }
            case "gira" -> {
                sendAction(new TurnHourglass());
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
                    case "engine" -> {
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
            default -> throw new TuiInputException("Comando non valido");

        }
    }
}
