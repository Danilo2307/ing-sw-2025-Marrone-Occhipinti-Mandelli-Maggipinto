package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

public interface EventVisitor<T> {

    public T visitForShipResponse(ShipResponse shipResponse, ViewAPI viewAPI);

    public T visitForTileResponse(TileResponse tileResponse, ViewAPI viewAPI);

    public T visitForUncoveredListResponse(UncoveredListResponse uncoveredListResponse, ViewAPI viewAPI);

    public T visitForStringResponse(StringResponse stringResponse, ViewAPI viewAPI);

    public T visitForStartGame(StartGame startGame, ViewAPI viewAPI);

    public T visitForEndGame(EndGame endGame, ViewAPI viewAPI);

    public T visitForTimeExpired(TimeExpired timeExpired, ViewAPI viewAPI);

    public T visitForEndTurn(EndTurn endTurn, ViewAPI viewAPI);

    public T visitForStateChanged(StateChanged stateChanged, ViewAPI viewAPI);

    public T visitForStartTurn(StartTurn startTurn, ViewAPI viewAPI);

    public T visitForErrorResponse(ErrorResponse errorResponse, ViewAPI viewAPI);

    public T visitForCannonShotIncoming(CannonShotIncoming cannonShotIncoming, ViewAPI viewAPI);

    public T visitForMeteorIncoming(MeteorIncoming meteorIncoming, ViewAPI viewAPI);

    public T visitForPlayerLanded(PlayerLanded playerLanded, ViewAPI viewAPI);

    public T visitForWrongUsername(WrongUsername wrongUsername, ViewAPI viewAPI);

    public T visitForAppropriateUsername(AppropriateUsername appropriateUsername, ViewAPI viewAPI);

    public T visitForRequestNumPlayers(RequestNumPlayers requestNumPlayers, ViewAPI viewAPI);

    public T visitForIllegalTruck(IllegalTruck illegalTruck, ViewAPI viewAPI);

    public T visitForUpdateFromCard(UpdateFromCard updateFromCard, ViewAPI viewAPI);

    public T visitForSelectLevel(SelectLevel selectLevel, ViewAPI viewAPI);

    public T visitForMatchFinished(MatchFinished matchFinished, ViewAPI viewAPI);

    public T visitForLobbyUnavailable(LobbyUnavailable lobbyUnavailable, ViewAPI viewAPI);

}
