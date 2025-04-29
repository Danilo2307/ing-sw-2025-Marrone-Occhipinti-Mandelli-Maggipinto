package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public interface EventInterface {
    public <T> T call(HandleEventVisitor<T> handleEventVisitor, TuiApplication tuiApplication);
}
