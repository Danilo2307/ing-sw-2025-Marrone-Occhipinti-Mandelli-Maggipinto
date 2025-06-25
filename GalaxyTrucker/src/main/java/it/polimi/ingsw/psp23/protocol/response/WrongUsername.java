package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.messages.ActionMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.SetUsername;
import it.polimi.ingsw.psp23.view.TUI.IOManager;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.Scanner;

/**
 * The WrongUsername class represents an event triggered when a user provides
 * an invalid or inappropriate username. It is part of the event handling system
 * in the application, implementing the Event interface and adhering to the Visitor
 * design pattern.
 *
 * This event is responsible for notifying the view layer when a username is deemed
 * incorrect. By invoking the handle method, the showWrongUsername method on the
 * provided ViewAPI instance is executed, informing the user about the invalid input.
 *
 */
public record WrongUsername() implements Event {

    public void handle(ViewAPI view) {
        view.showWrongUsername();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForWrongUsername(this, viewAPI);
    }

}
