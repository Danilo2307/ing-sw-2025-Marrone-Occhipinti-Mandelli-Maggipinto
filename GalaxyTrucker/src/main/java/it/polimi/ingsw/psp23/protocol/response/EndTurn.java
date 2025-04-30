package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record EndTurn() implements Event {

    public void handle(TuiApplication tuiApplication){
        tuiApplication.getIOManager().print("Turn ended");
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForEndTurn(this, tuiApplication);
    }

}
