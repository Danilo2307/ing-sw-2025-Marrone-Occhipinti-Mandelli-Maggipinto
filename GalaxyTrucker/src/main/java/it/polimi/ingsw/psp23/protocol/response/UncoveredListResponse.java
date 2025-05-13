package it.polimi.ingsw.psp23.protocol.response;


import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.view.TUI.IOManager;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.ArrayList;

public record UncoveredListResponse(ArrayList<Component> uncovered, int lastVersion) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showUncovered(uncovered, lastVersion);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForUncoveredListResponse(this, viewAPI);
    }

}