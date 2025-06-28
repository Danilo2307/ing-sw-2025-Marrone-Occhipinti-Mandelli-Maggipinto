package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

public record MatchAbandoned(String message) implements Event {
    public void handle(ViewAPI viewAPI) {
        viewAPI.stopMatch(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForMatchAbandoned(this, viewAPI);
    }
}
