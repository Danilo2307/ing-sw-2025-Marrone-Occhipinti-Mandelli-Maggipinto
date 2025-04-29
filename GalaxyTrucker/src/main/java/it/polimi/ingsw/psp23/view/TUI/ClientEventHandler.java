package it.polimi.ingsw.psp23.view.TUI;


import it.polimi.ingsw.psp23.protocol.response.*;
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

        e.call(new HandleEventVisitor(), tui);

    }

    @Override
    public void messageReceived(Event event) {
        /** qui arrivranno i messaggi diretti a tutti i client */
    }

}
