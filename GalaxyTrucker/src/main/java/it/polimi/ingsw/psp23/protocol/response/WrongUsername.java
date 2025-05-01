package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.view.TUI.IOManager;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record WrongUsername() implements Event {

    public void handle(TuiApplication tui) {
        tui.getIOManager().print("Username errato, inseriscine uno nuovo:\n");
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForWrongUsername(this, tuiApplication);
    }

}
