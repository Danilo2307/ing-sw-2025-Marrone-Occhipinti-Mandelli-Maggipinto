package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.TUI.TuiState;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * This class represents an event indicating that a username and its associated level
 * are deemed appropriate. It contains the username and level information and interacts
 * with a view to notify the user accordingly.
 *
 * Implements the Event interface to provide specific functionality for event processing
 * and handling via an event visitor.
 *
 * Methods:
 * - handle(ViewAPI view): Processes the event by calling the appropriate method in the provided view
 *   to display the username and level.
 * - call(EventVisitor<T> eventVisitor, ViewAPI viewAPI): Accepts an event visitor to handle
 *   this type of event with the provided view.
 * - toString(): Provides a textual representation of the object, returning a brief description.
 */
public record AppropriateUsername(String username, int level) implements Event {

    public void handle(ViewAPI view) {
        view.showAppropriateUsername(username, level);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForAppropriateUsername(this, viewAPI);
    }

    @Override
    public String toString() {
        return "Appropriate Username";
    }

}
