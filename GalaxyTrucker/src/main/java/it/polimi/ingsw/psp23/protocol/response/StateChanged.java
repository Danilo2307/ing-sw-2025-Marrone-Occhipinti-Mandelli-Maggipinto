package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.TUI.TuiState;
import it.polimi.ingsw.psp23.view.ViewAPI;


public record StateChanged(GameStatus newState) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.stateChanged(newState);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForStateChanged(this, viewAPI);
    }

}
