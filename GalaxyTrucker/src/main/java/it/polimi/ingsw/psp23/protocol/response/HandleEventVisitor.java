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

}
