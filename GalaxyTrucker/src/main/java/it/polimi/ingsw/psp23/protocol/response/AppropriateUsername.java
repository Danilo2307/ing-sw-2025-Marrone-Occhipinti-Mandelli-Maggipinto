package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.TUI.TuiState;
import it.polimi.ingsw.psp23.view.ViewAPI;

public record AppropriateUsername(String username) implements Event {

    public void handle(ViewAPI view) {
        view.showAppropriateUsername(username);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForAppropriateUsername(this, viewAPI);
    }

    @Override
    public String toString() {
        return "Appropriate Username";
    }

}
