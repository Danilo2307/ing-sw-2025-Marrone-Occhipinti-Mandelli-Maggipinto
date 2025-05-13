package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

public record ErrorResponse(String message) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showError(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI) {
        return eventVisitor.visitForErrorResponse(this, viewAPI);
    }

}
