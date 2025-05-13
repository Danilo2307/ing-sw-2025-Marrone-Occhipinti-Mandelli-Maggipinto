package it.polimi.ingsw.psp23.view.TUI;


import it.polimi.ingsw.psp23.protocol.response.*;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.socket.MessageObserver;
import it.polimi.ingsw.psp23.view.ViewAPI;

import javax.swing.text.View;

public class ClientEventHandler implements MessageObserver {
    private final ViewAPI view;

    public ClientEventHandler(ViewAPI view) {
        this.view = view;
    }

    public void handle(Event e) {

        e.call(new HandleEventVisitor(), view);

    }

    @Override
    public void messageReceived(Event event) {
        /** qui arrivranno i messaggi diretti a tutti i client */
    }

}
