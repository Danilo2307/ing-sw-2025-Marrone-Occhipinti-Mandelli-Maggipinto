package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.events.*;
import it.polimi.ingsw.psp23.exceptions.TuiInputException;
import it.polimi.ingsw.psp23.network.socket.Client;

/** Flusso generale dell'app: loop principale per input, mapping comandi utente -> chiamata a metodo ClientController,
 *  cambio stato. */
public class TuiApplication {
    private String username;
    private ClientController cc;
    private int lastUncoveredVersion;
    private IOManager io;

    /// TODO: costruttore: capire bene cosa mettere

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
                    cc.sendEvent(new DrawFromHeap(username));
                }
                else if (words[1].equals("scoperta")) {
                    int index = Integer.parseInt(words[2]);
                    cc.sendEvent(new DrawFromFaceUp(username, index, lastUncoveredVersion));
                }
                else
                    throw new TuiInputException("Comando non valido");
            }
            case "scoperte" -> {
                cc.sendEvent(new RequestUncovered(username));
            }
            case "salda" -> {
                // TODO: inserire controlli eccezioni eccetera
                int x = Integer.parseInt(words[1]);
                int y = Integer.parseInt(words[2]);
                cc.sendEvent(new AddTile(username, x, y));
            }
            case "rilascia" -> {
                cc.sendEvent(new ReleaseTile(username));
            }
            case "ruota" -> {
                cc.sendEvent(new RotateTile(username));
            }
            case "gira" -> {
                cc.sendEvent(new TurnHourglass(username));
            }
            case "mostra" -> {
                cc.sendEvent(new RequestShip(username));
            }
            case "info" -> {
                int x = Integer.parseInt(words[1]);
                int y = Integer.parseInt(words[2]);
                cc.sendEvent(new RequestTileInfo(username, x, y));
            }
        }
    }

}
