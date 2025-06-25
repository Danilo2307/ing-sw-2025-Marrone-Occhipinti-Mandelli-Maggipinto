// Questo evento verrà mandato dal server ai client e conterranno nel parametro message la stringa di ritorno del metodo
// describe degli eventi del model che notificano aggiornamenti delle carte

package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * The UpdateFromCard class represents an event that conveys an update related
 * to a card in the application. It implements the Event interface and utilizes
 * the Visitor design pattern to enable processing by an EventVisitor.
 *
 * This event is associated with a specific message that describes the card-related
 * update, which is provided during the construction of the event.
 *
 * When the handle method is invoked, it executes the showCardUpdate method on
 * the provided ViewAPI instance, passing the associated message to ensure the
 * update is communicated to the view layer.
 *
 * The call method allows the event to be processed further by an implementation
 * of the EventVisitor interface, enabling specific behaviors or operations based
 * on this event type.
 *
 * @param message The message describing the card-related update.
 */
public record UpdateFromCard(String message) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showCardUpdate(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForUpdateFromCard(this, viewAPI);
    }
}
