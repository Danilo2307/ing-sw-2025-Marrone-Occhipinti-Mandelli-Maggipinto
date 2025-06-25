package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * Represents an event indicating that a lobby is unavailable.
 * This event is used to signal the application to handle the
 * unavailability of a lobby, typically resulting in a system exit.
 *
 * This class implements the {@link Event} interface to ensure compatibility
 * with the application's event handling system, allowing it to be processed
 * by an {@link EventVisitor}.
 */
public class LobbyUnavailable implements Event{

    public void handle(ViewAPI viewAPI) {
        System.exit(0);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForLobbyUnavailable(this, viewAPI);
    }
}
