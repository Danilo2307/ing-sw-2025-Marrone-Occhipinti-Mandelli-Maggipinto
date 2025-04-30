package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

import javax.swing.plaf.nimbus.State;

public record StateChanged(State newState) implements Event {

    public void handle(TuiApplication tuiApplication) {
        tuiApplication.getIOManager().print("State changed to " + newState);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForStateChanged(this, tuiApplication);
    }

}
