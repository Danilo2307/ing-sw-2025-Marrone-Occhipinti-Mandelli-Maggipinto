package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * This record represents the event that occurs when a match has finished.
 * It contains a message providing details about the conclusion of the match.
 *
 * The `MatchFinished` event implements the {@link Event} interface, allowing it
 * to be used within an event-driven system. When handled, it communicates
 * the match's completion to the provided {@link ViewAPI} and performs any necessary
 * operations through the {@link EventVisitor}.
 *
 */
public record MatchFinished(String message) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.endMatch(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForMatchFinished(this, viewAPI);
    }

}
