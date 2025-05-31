package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

public record IncorrectWelding() implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.incorrectTile();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForIncorrectWelding(this, viewAPI);
    }
}
