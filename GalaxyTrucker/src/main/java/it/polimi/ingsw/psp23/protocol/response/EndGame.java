package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record EndGame() implements Event {

    public void handle(TuiApplication tui) {
        tui.getIOManager().print("Partita terminata");
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForEndGame(this, tuiApplication);
    }
}
