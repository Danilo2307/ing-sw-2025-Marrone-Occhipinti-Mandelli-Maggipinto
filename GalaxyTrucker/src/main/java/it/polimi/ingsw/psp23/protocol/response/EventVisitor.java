package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public interface EventVisitor<T> {

    public T visitForShipResponse(ShipResponse shipResponse, TuiApplication tuiApplication);

    public T visitForTileResponse(TileResponse tileResponse, TuiApplication tuiApplication);

    public T visitForUncoveredListResponse(UncoveredListResponse uncoveredListResponse, TuiApplication tuiApplication);

    public T visitForStringResponse(StringResponse stringResponse, TuiApplication tuiApplication);

    public T visitForStartGame(StartGame startGame, TuiApplication tuiApplication);

    public T visitForEndGame(EndGame endGame, TuiApplication tuiApplication);

    public T visitForTimeExpired(TimeExpired timeExpired, TuiApplication tuiApplication);

    public T visitForEndTurn(EndTurn endTurn, TuiApplication tuiApplication);

    public T visitForStateChanged(StateChanged stateChanged, TuiApplication tuiApplication);

    public T visitForStartTurn(StartTurn startTurn, TuiApplication tuiApplication);

    public T visitForErrorResponse(ErrorResponse errorResponse, TuiApplication tuiApplication);

    public T visitForCannonShotIncoming(CannonShotIncoming cannonShotIncoming, TuiApplication tuiApplication);

    public T visitForMeteorIncoming(MeteorIncoming meteorIncoming, TuiApplication tuiApplication);

    public T visitForPlayerLanded(PlayerLanded playerLanded, TuiApplication tuiApplication);

    public T visitForWrongUsername(WrongUsername wrongUsername, TuiApplication tuiApplication);

    public T visitForAppropriateUsername(AppropriateUsername appropriateUsername, TuiApplication tuiApplication);

    public T visitForRequestNumPlayers(RequestNumPlayers requestNumPlayers, TuiApplication tuiApplication);

}
