package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * The TimeExpired class represents an event indicating that a specific time period
 * has expired. This event implements the Event interface and is designed to be
 * processed using the Visitor design pattern.
 *
 * When the handle method is invoked, the event triggers the showTimeExpired method
 * on the provided ViewAPI instance, allowing the application to communicate the expiration
 * of the allocated time to the user interface layer.
 *
 */
public record TimeExpired() implements Event {

    public void handle(ViewAPI viewAPI){
        viewAPI.showTimeExpired();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForTimeExpired(this, viewAPI);
    }

}
