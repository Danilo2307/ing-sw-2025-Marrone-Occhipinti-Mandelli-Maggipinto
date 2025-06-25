package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.ArrayList;

/**
 * The VisibleDeckResponse class represents an event for displaying a deck of cards to the user
 * in the application. It implements the Event interface and utilizes the Visitor design pattern
 * for processing by an EventVisitor.
 *
 * This event provides information about the IDs of cards in a visible deck and a description
 * of the current state or context of the deck. It is responsible for notifying the view layer
 * to display this deck using the provided ViewAPI instance.
 *
 * The call method allows further processing of this event by an EventVisitor, enabling customized
 * behavior or operations related to this specific event type.
 *
 * @param idCards     A list of integer IDs representing the cards in the visible deck.
 * @param description A string providing a description of the current state or context of the deck.
 */
public record VisibleDeckResponse(ArrayList<Integer> idCards, String description) implements Event {

    public void handle(ViewAPI view) {
        view.showDeck(idCards, description);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForVisibleDeckResponse(this, viewAPI);
    }
}
