package it.polimi.ingsw.psp23.view.TUI;


import it.polimi.ingsw.psp23.protocol.response.Event;
import it.polimi.ingsw.psp23.protocol.response.ShipResponse;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;
import it.polimi.ingsw.psp23.protocol.response.UncoveredListResponse;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.socket.MessageObserver;

public class ClientEventHandler implements MessageObserver {
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
                    io.print(io.getSymbol(c));
                }
            }
            default -> {}
        }
    }

    @Override
    public void messageReceived(Event event) {
        /** qui arrivranno i messaggi diretti a tutti i client */
    }

}
