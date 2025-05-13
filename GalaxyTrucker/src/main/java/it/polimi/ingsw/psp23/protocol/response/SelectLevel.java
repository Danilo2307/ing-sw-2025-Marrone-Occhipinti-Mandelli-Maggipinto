package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

public record SelectLevel() implements Event {

    public void handle(ViewAPI view) {
        view.showRequestLevel();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForSelectLevel(this, viewAPI);
    }

}
