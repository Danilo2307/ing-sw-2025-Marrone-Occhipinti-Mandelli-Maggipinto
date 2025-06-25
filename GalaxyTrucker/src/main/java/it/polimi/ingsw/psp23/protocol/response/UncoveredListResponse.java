package it.polimi.ingsw.psp23.protocol.response;


import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.view.TUI.IOManager;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.ArrayList;

/**
 * Represents a response containing a list of uncovered components and the associated version number.
 * This response is designed to interact with the user interface (via the {@link ViewAPI})
 * and process the event using the visitor pattern (via {@link EventVisitor}).
 *
 * @param uncovered   The list of uncovered {@link Component} objects.
 * @param lastVersion The version number associated with the uncovered components.
 */
public record UncoveredListResponse(ArrayList<Component> uncovered, int lastVersion) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showUncovered(uncovered, lastVersion);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForUncoveredListResponse(this, viewAPI);
    }

}