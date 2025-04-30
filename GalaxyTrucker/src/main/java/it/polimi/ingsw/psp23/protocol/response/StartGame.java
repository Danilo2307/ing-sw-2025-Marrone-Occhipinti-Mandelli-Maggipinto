package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record StartGame() implements Event {

    public void handle(TuiApplication tui) {
        tui.getIOManager().print("Starting game event");
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForStartGame(this, tuiApplication);
    }

}
