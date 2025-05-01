package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record PlayerLanded(String username, int index) implements Event {

    public void handle(TuiApplication tuiApplication){
        tuiApplication.getIOManager().print("Il giocatore " + username + " e' atterrato sul pianeta numero " + (index + 1));
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForPlayerLanded(this, tuiApplication);
    }

}
