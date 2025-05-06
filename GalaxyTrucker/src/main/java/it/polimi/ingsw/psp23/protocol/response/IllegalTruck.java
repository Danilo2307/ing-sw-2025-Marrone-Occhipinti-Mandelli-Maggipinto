package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record IllegalTruck() implements Event {

    public void handle(TuiApplication tuiApplication){
        tuiApplication.getIOManager().error("La tua nave non rispetta i criteri di Galaxy Trucker, sistemala e poi digita 'corretta'\n");
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForIllegalTruck(this, tuiApplication);
    }

}
