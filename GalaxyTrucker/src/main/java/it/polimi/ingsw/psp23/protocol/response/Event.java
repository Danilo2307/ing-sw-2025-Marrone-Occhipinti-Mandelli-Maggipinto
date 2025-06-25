package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.io.Serializable;

/**
 * Represents a general event that can be processed by an {@link EventVisitor}
 * and communicated to a given {@link ViewAPI} for handling or display purposes.
 * This interface is implemented by various event types representing different
 * states or triggers within the application.
 *
 * The design uses the Visitor pattern to allow the events to be processed
 * by different types of event visitors based on the specific type of event.
 */
public interface Event extends Serializable {

    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI);

    public String toString();

}
