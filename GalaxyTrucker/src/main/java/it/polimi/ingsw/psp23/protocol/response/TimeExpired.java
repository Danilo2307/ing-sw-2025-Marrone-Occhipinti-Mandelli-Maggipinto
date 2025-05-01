package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record TimeExpired() implements Event {

    public void handle(TuiApplication tui){
        tui.getIOManager().print("Tempo scaduto");
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForTimeExpired(this, tuiApplication);
    }

}
