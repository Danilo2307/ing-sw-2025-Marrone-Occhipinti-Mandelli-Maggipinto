package it.polimi.ingsw.psp23.view;


import it.polimi.ingsw.psp23.protocol.response.*;
import it.polimi.ingsw.psp23.network.socket.MessageObserver;

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
