package it.polimi.ingsw.psp23.protocol.response;


import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

/** response event sent from the server; sends the component requested from the client */
public record TileResponse(Component requested) implements Event {
    public void handle(TuiApplication tui) {
        tui.getIOManager().printInfoTile(requested);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForTileResponse(this, tuiApplication);
    }

}
