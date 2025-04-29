package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.events.server.ShipResponse;
import it.polimi.ingsw.psp23.events.server.StringResponse;
import it.polimi.ingsw.psp23.events.server.TileResponse;
import it.polimi.ingsw.psp23.events.server.UncoveredListResponse;

public interface HandleEventVisitor<T> {

    public T visitForShipResponse(ShipResponse shipResponse, TuiApplication TUIApplication);

    public T visitForTileResponse(TileResponse tileResponse, TuiApplication TUIApplication);

    public T visitForUncoveredListResponse(UncoveredListResponse uncoveredListResponse, TuiApplication TUIApplication);

    public T visitForStringResponse(StringResponse stringResponse, TuiApplication TUIApplication);

}
