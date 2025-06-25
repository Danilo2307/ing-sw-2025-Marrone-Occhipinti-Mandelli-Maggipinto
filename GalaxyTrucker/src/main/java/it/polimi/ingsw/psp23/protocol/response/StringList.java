package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.List;

/**
 * The StringList class represents an event that provides a list of player names.
 * It implements the Event interface and employs the Visitor design pattern
 * to allow processing by an {@link EventVisitor}.
 *
 * This event is designed to communicate a list of player names to the view layer
 * through a given {@link ViewAPI} instance. The main function of this event
 * is to enable the saving of the provided list of player names in the view.
 *
 * The handle method, when invoked, calls the savePlayersNames method on the
 * provided ViewAPI instance to save the list of player names.
 *
 * The call method enables further processing of this event through an
 * EventVisitor implementation. This allows for custom handling or operations
 * based on this specific type of event.
 *
 * @param players The list of player names to be communicated to the ViewAPI.
 */
public record StringList(List<String> players) implements Event {

        public void handle(ViewAPI viewAPI) {
            viewAPI.savePlayersNames(players);
        }

        @Override
        public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
            return eventVisitor.visitForStringList(this, viewAPI);
        }


}
