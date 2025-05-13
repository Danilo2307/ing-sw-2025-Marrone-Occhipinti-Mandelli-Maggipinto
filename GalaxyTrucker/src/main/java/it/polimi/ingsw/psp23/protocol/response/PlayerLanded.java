package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

public record PlayerLanded(String username, int index) implements Event {

    public void handle(ViewAPI viewAPI){
        viewAPI.showPlayerLanded(username, index);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI) {
        return eventVisitor.visitForPlayerLanded(this, viewAPI);
    }

}
