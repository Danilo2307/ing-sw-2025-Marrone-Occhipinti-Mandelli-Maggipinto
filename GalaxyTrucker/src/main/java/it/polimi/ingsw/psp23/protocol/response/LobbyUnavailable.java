package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

public class LobbyUnavailable implements Event{

    public void handle(ViewAPI viewAPI) {
        System.exit(0);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForLobbyUnavailable(this, viewAPI);
    }
}
