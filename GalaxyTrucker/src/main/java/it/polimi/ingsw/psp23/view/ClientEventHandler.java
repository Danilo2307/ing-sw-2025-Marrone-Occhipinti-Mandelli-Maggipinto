package it.polimi.ingsw.psp23.view;


import it.polimi.ingsw.psp23.protocol.response.*;
import it.polimi.ingsw.psp23.network.socket.MessageObserver;

/**
 * The ClientEventHandler class is responsible for processing events received
 * from the server and delegating their handling to the appropriate methods
 * within the client's view implementation. It makes use of the Visitor
 * design pattern to ensure that events are processed using the appropriate
 * visitor implementation provided by the {@link HandleEventVisitor}.
 */
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

    public ViewAPI getView() {
        return view;
    }
}
