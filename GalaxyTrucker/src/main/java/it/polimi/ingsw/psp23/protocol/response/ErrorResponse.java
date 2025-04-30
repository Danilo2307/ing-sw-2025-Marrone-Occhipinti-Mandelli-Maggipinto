package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record ErrorResponse(String message) implements Event {

    public void handle(TuiApplication tui) {
        tui.getIOManager().print(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication) {
        return eventVisitor.visitForErrorResponse(this, tuiApplication);
    }

}
