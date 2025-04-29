package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.model.cards.VisitorParametrico;

public interface EventInterface {
    public <T> T call(HandleEventVisitor<T> handleEventVisitor, TuiApplication tuiApplication);
}
