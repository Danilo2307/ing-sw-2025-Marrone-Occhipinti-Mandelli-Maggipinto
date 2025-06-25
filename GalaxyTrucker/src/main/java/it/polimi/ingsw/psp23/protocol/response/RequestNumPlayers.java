package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * This class represents an event that prompts the user to specify the number of players
 * for a game. It is part of the event-handling system that uses the Visitor design pattern
 * to manage interactions between different components of the application.
 *
 * The {@code RequestNumPlayers} event, when handled, instructs the {@link ViewAPI}
 * to display a prompt for the user to input the desired number of players for the game session.
 *
 * The class implements the {@link Event} interface, which allows it to be processed by
 * an {@link EventVisitor} for specific handling logic, as well as communicated to the view layer
 * via the {@link ViewAPI}.
 */
public record RequestNumPlayers() implements Event {

    public void handle(ViewAPI view) {
        view.showRequestNumPlayers();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForRequestNumPlayers(this, viewAPI);
    }

}
