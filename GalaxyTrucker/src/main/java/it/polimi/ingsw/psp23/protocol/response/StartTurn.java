package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * The StartTurn class represents an event that signals the beginning of a
 * player's turn in the application. It implements the Event interface and
 * uses the Visitor design pattern to allow processing by an EventVisitor.
 *
 * This event is associated with a specific player, identified through their
 * username, and is responsible for notifying the view layer that the player's
 * turn has started.
 *
 * When the handle method is called, the showTurn method on the provided
 * ViewAPI instance is invoked to communicate the start of the turn to the view.
 *
 * The call method enables additional processing of this event by an EventVisitor
 * implementation, allowing customized behavior or operations based on this
 * specific event type.
 *
 * @param username The username of the player whose turn has started.
 */
public record StartTurn(String username) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showTurn(username);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForStartTurn(this, viewAPI);
    }

}
