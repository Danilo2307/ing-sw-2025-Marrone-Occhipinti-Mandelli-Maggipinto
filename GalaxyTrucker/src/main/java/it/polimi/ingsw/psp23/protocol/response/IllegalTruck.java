package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * Represents an event that signals the occurrence of an illegal truck action in the application.
 * This event is used to notify the {@link ViewAPI} to display the corresponding message or perform
 * related actions and is also visited by an {@link EventVisitor} for further processing.
 *
 * The class follows the Visitor pattern, enabling dynamic handling of this type of event
 * by delegating the behavior to the appropriate visitor.
 */
public record IllegalTruck() implements Event {

    public void handle(ViewAPI viewAPI){
        viewAPI.showIllegalTruck();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForIllegalTruck(this, viewAPI);
    }

}
