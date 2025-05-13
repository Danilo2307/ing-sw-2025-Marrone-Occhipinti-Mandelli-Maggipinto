package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

public record StartTurn(String username) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showTurn(username);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForStartTurn(this, viewAPI);
    }

}
