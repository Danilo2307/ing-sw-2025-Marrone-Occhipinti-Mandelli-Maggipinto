package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record SelectLevel() implements Event {

    public void handle(TuiApplication tui) {
        tui.getIOManager().print("Inserisci il livello di difficoltà a cui vuoi giocare (0 o 2): ");
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForSelectLevel(this, tuiApplication);
    }

}
