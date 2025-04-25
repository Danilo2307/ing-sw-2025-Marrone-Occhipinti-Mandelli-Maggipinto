package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.events.Event;
import it.polimi.ingsw.psp23.events.server.ShipResponse;
import it.polimi.ingsw.psp23.events.server.TileResponse;
import it.polimi.ingsw.psp23.events.server.UncoveredListResponse;
import it.polimi.ingsw.psp23.model.components.Component;

public class ClientEventHandler {
    private final IOManager io;
    private TuiApplication tui;

    public ClientEventHandler(IOManager io, TuiApplication tui) {
        this.io = io;
        this.tui = tui;
    }

    public void handle(Event e) {

        switch(e) {
            case TileResponse tr -> io.printInfoTile(tr.requested());
            case ShipResponse sr -> io.printShip(sr.ship());
            case UncoveredListResponse ur -> {
                tui.setLastUncoveredVersion(ur.lastVersion());
                for (Component c: ur.uncovered()) {
                    io.printInfoTile(c);
                }
            }
            default -> {}
        }
    }
}
