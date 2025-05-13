package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

public record ShipResponse(Component[][] ship, int[][] validCoordinates) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showShip(ship, validCoordinates);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForShipResponse(this, viewAPI);
    }

}