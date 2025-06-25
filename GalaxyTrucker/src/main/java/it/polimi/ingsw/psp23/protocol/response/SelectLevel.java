package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * The SelectLevel class represents an event triggered when a level selection is required in the application.
 * It implements the Event interface and follows the Visitor design pattern to process the event
 * through an EventVisitor implementation.
 *
 * This event is responsible for requesting the level selection from the user
 * via the provided ViewAPI instance.
 *
 * The SelectLevel event invokes specific behaviors on the view layer to display
 * the necessary prompt for the user to select a level.
 */
public record SelectLevel() implements Event {

    public void handle(ViewAPI view) {
        view.showRequestLevel();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForSelectLevel(this, viewAPI);
    }

}
