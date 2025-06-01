package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

public record NewCardDrawn(int id, String descrption) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showNewCard(id, descrption);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForNewCardDrawn(this, viewAPI);
    }

}
