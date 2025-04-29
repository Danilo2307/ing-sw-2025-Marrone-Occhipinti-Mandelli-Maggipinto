package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public interface HandleEventVisitor<T> {

    public T visitForShipResponse(ShipResponse shipResponse, TuiApplication TUIApplication);

    public T visitForTileResponse(TileResponse tileResponse, TuiApplication TUIApplication);

    public T visitForUncoveredListResponse(UncoveredListResponse uncoveredListResponse, TuiApplication TUIApplication);

    public T visitForStringResponse(StringResponse stringResponse, TuiApplication TUIApplication);

}
