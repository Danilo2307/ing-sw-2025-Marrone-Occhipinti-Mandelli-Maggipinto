package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * Represents an error response in the system.
 * It encapsulates a message describing the error and provides
 * methods to handle the error and allow visitor-based processing.
 *
 * This record implements the {@code Event} interface, enabling it
 * to be used in event-driven architectures. The error message is
 * displayed using the provided {@link ViewAPI} interface methods.
 */
public record ErrorResponse(String message) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showError(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI) {
        return eventVisitor.visitForErrorResponse(this, viewAPI);
    }

}
