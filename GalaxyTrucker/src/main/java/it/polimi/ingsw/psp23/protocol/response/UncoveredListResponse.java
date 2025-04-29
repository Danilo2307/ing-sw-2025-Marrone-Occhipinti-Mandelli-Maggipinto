package it.polimi.ingsw.psp23.protocol.response;


import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.view.TUI.IOManager;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

import java.util.ArrayList;

public record UncoveredListResponse(ArrayList<Component> uncovered, int lastVersion) implements Event {

    public void handle(TuiApplication tui) {
        tui.setLastUncoveredVersion(lastVersion);
        for (Component component : uncovered) {
            IOManager ioManager = tui.getIOManager();
            ioManager.print(ioManager.getSymbol(component));
        }
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForUncoveredListResponse(this, tuiApplication);
    }

}