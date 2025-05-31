package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.ArrayList;

public record VisibleDeckResponse(ArrayList<Integer> idCards, String description) implements Event {

    public void handle(ViewAPI view) {
        view.showDeck(idCards, description);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForVisibleDeckResponse(this, viewAPI);
    }
}
