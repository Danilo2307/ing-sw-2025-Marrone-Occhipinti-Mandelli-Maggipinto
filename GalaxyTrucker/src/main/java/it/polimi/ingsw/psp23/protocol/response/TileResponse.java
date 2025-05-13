package it.polimi.ingsw.psp23.protocol.response;


import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

import javax.swing.text.View;

/** response event sent from the server; sends the component requested from the client */
public record TileResponse(Component requested) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showTile(requested);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForTileResponse(this, viewAPI);
    }

}
