package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

public class HandleEventVisitor implements EventVisitor<Void>{

    @Override
    public Void visitForShipResponse(ShipResponse shipResponse, TuiApplication tuiApplication){
        shipResponse.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForTileResponse(TileResponse tileResponse, TuiApplication tuiApplication){
        tileResponse.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForUncoveredListResponse(UncoveredListResponse uncoveredListResponse, TuiApplication tuiApplication){
        uncoveredListResponse.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForStringResponse(StringResponse stringResponse, TuiApplication tuiApplication){
        stringResponse.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForStartGame(StartGame startGame, TuiApplication tuiApplication){
        startGame.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForEndGame(EndGame endGame, TuiApplication tuiApplication){
        endGame.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForTimeExpired(TimeExpired timeExpired, TuiApplication tuiApplication){
        timeExpired.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForEndTurn(EndTurn endTurn, TuiApplication tuiApplication){
        endTurn.handle(tuiApplication);
        return null;

    }

    @Override
    public Void visitForStateChanged(StateChanged stateChanged, TuiApplication tuiApplication){
        stateChanged.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForStartTurn(StartTurn startTurn, TuiApplication tuiApplication){
        startTurn.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForErrorResponse(ErrorResponse errorResponse, TuiApplication tuiApplication){
        errorResponse.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForCannonShotIncoming(CannonShotIncoming cannonShotIncoming, TuiApplication tuiApplication){
        cannonShotIncoming.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForMeteorIncoming(MeteorIncoming meteorIncoming, TuiApplication tuiApplication){
        meteorIncoming.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForPlayerLanded(PlayerLanded playerLanded, TuiApplication tuiApplication){
        playerLanded.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForWrongUsername(WrongUsername wrongUsername, TuiApplication tuiApplication){
        wrongUsername.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForAppropriateUsername(AppropriateUsername appropriateUsername, TuiApplication tuiApplication){
        appropriateUsername.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForRequestNumPlayers(RequestNumPlayers requestNumPlayers, TuiApplication tuiApplication){
        requestNumPlayers.handle(tuiApplication);
        return null;
    }

    @Override
    public Void visitForIllegalTruck(IllegalTruck illegalTruck, TuiApplication tuiApplication){
        illegalTruck.handle(tuiApplication);
        return null;
    }

    public Void visitForUpdateFromCard(UpdateFromCard updateFromCard, TuiApplication tuiApplication){
        updateFromCard.handle(tuiApplication);
        return null;
    }

    public Void visitForSelectLevel(SelectLevel selectLevel, TuiApplication tuiApplication){
        selectLevel.handle(tuiApplication);
        return null;
    }

}
