package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.TUI.TuiState;
import it.polimi.ingsw.psp23.view.ViewAPI;


/**
 * Represents the event triggered when the game's state changes.
 * This class implements the Event interface and uses the Visitor pattern
 * to facilitate processing by an EventVisitor instance.
 *
 * Responsibilities of this event include:
 * - Notifying the provided ViewAPI instance about the change in state
 *   via the stateChanged method.
 * - Facilitating its processing by an EventVisitor through the call method,
 *   which delegates the handling logic to the event visitor instance.
 *
 * @param newState the updated state of the game, represented as a GameStatus object.
 */
public record StateChanged(GameStatus newState) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.stateChanged(newState);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForStateChanged(this, viewAPI);
    }

}
