package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.events.Event;
import it.polimi.ingsw.psp23.events.server.ShipResponse;
import it.polimi.ingsw.psp23.events.server.TileResponse;

public class ClientEventHandler {
    private final IOManager io;

    public ClientEventHandler(IOManager io) {
        this.io = io;
    }

    public void handle(Event e) {

        switch(e) {
            case TileResponse tr -> io.printInfoTile(tr.requested());
            case ShipResponse sr -> io.printShip(sr.ship());
            default -> {}
        }
    }
}
