package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * The StartGame class represents an event triggered at the beginning of the game.
 * It implements the Event interface and uses the Visitor design pattern for processing
 * by an {@link EventVisitor}.
 *
 * This event is responsible for signaling the start of a game by invoking the
 * appropriate behavior on a given {@link ViewAPI} instance.
 *
 * When the handle method is called, it executes the showStart method on the
 * provided ViewAPI instance to display or handle the start game operation
 * at the view layer.
 */
public record StartGame() implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showStart();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForStartGame(this, viewAPI);
    }

}
