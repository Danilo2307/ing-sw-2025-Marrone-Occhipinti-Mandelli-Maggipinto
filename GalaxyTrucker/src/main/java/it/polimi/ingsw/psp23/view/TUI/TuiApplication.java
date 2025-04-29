package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.events.*;
import it.polimi.ingsw.psp23.exceptions.TuiInputException;
import it.polimi.ingsw.psp23.network.socket.Client;

/** Flusso generale dell'app: loop principale per input, mapping comandi utente -> chiamata a metodo ClientController,
 *  cambio stato. */
public class TuiApplication {
    private Client client;
    // private ClientController cc;  ora useless perchè sendEvent è in questa classe
    private int lastUncoveredVersion;
    private IOManager io;

    /// TODO: capire costruttore (client=null)

    public void setClient(Client client) {
        this.client = client;
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

    public void sendEvent(Action action) {
        EventMessage eventMessage = new EventMessage(action);
        if (client != null) {
            client.sendMessage(eventMessage);
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
                    sendEvent(new DrawFromHeap());
                }
                else if (words[1].equals("scoperta")) {
                    int index = Integer.parseInt(words[2]);
                    sendEvent(new DrawFromFaceUp(index, lastUncoveredVersion));
                }
                else
                    throw new TuiInputException("Comando non valido");
            }
            case "scoperte" -> {
                sendEvent(new RequestUncovered());
            }
            case "salda" -> {
                // TODO: inserire controlli eccezioni eccetera
                int x = Integer.parseInt(words[1]);
                int y = Integer.parseInt(words[2]);
                sendEvent(new AddTile(x, y));
            }
            case "rilascia" -> {
                sendEvent(new ReleaseTile());
            }
            case "ruota" -> {
                sendEvent(new RotateTile());
            }
            case "gira" -> {
                sendEvent(new TurnHourglass());
            }
            case "mostra" -> {
                sendEvent(new RequestShip());
            }
            case "info" -> {
                int x = Integer.parseInt(words[1]);
                int y = Integer.parseInt(words[2]);
                sendEvent(new RequestTileInfo(x, y));
            }
        }
    }
}
