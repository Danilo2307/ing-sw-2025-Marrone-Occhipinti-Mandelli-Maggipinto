package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.TUI.TuiState;

public record AppropriateUsername(String username) implements Event {

    public void handle(TuiApplication tui) {
        tui.getClient().getSocketHandler().setUsername(username);
        tui.getIOManager().print("Benvenuto in Galaxy Trucker!!\n");
        tui.setState(TuiState.LOBBY);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForAppropriateUsername(this, tuiApplication);
    }

    @Override
    public String toString() {
        return "Appropriate Username";
    }

}
