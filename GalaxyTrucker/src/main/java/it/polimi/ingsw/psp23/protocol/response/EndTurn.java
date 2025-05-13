package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

public record EndTurn(String username) implements Event {

    public void handle(ViewAPI viewAPI){
        viewAPI.showEndTurn(username);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForEndTurn(this, viewAPI);
    }

}
