package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public interface EventVisitor<T> {

    public T visitForShipResponse(ShipResponse shipResponse, TuiApplication tuiApplication);

    public T visitForTileResponse(TileResponse tileResponse, TuiApplication tuiApplication);

    public T visitForUncoveredListResponse(UncoveredListResponse uncoveredListResponse, TuiApplication tuiApplication);

    public T visitForStringResponse(StringResponse stringResponse, TuiApplication tuiApplication);

}
