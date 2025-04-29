package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public record ShipResponse(Component[][] ship) implements Event {
    public void handle(TuiApplication tui) {
        tui.getIOManager().printShip(ship);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication){
        return eventVisitor.visitForShipResponse(this, tuiApplication);
    }

}