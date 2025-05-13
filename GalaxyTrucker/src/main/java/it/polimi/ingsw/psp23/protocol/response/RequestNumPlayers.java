package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

public record RequestNumPlayers() implements Event {

    public void handle(ViewAPI view) {
        view.showRequestNumPlayers();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForRequestNumPlayers(this, viewAPI);
    }

}
