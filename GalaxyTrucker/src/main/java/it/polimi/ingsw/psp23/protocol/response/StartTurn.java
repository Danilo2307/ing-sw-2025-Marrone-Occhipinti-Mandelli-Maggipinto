package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record StartTurn(String username) implements Event {

    public void handle(TuiApplication tuiApplication) {
        tuiApplication.getIOManager().print("Turno di " + username + " iniziato");
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForStartTurn(this, tuiApplication);
    }

}
