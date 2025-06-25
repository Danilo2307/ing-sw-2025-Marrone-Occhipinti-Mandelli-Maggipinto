package it.polimi.ingsw.psp23.protocol.response;


import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

import javax.swing.text.View;

/**
 * Represents a response event that involves handling a specific tile represented by a {@code Component}.
 * This response is typically invoked when a request for a tile has been processed.
 *
 * Implements the {@link Event} interface, enabling it to be processed via the visitor pattern
 * with an {@link EventVisitor}.
 *
 * @param requested the {@link Component} that represents the requested tile in the system.
 */
public record TileResponse(Component requested) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showTile(requested);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForTileResponse(this, viewAPI);
    }

}
