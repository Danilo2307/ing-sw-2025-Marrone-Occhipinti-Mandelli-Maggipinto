package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.exceptions.TuiInputException;
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
                // TODO: inserire controlli eccezioni eccetera
                int x = Integer.parseInt(words[1]);
                int y = Integer.parseInt(words[2]);
                sendAction(new AddTile(x, y));
            }
            case "rilascia" -> {
                sendAction(new ReleaseTile());
            }
            case "ruota" -> {
                sendAction(new RotateTile());
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
        }
    }
}
