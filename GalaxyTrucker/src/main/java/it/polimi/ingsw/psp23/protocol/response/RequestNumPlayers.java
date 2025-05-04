package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record RequestNumPlayers() implements Event {

    public void handle(TuiApplication tui) {
        tui.getIOManager().print("Inserisci il numero di giocatori che faranno parte della partita:\n");
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForRequestNumPlayers(this, tuiApplication);
    }

}
