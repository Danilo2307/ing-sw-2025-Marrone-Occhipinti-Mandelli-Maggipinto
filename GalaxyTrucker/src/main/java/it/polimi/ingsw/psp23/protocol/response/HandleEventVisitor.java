package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;
import javafx.css.Match;

public class HandleEventVisitor implements EventVisitor<Void>{

    @Override
    public Void visitForShipResponse(ShipResponse shipResponse, ViewAPI viewAPI){
        shipResponse.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForTileResponse(TileResponse tileResponse, ViewAPI viewAPI){
        tileResponse.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForUncoveredListResponse(UncoveredListResponse uncoveredListResponse, ViewAPI viewAPI){
        uncoveredListResponse.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForStringResponse(StringResponse stringResponse, ViewAPI viewAPI){
        stringResponse.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForStartGame(StartGame startGame, ViewAPI viewAPI){
        startGame.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForEndGame(EndGame endGame, ViewAPI viewAPI){
        endGame.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForTimeExpired(TimeExpired timeExpired, ViewAPI viewAPI){
        timeExpired.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForEndTurn(EndTurn endTurn, ViewAPI viewAPI){
        endTurn.handle(viewAPI);
        return null;

    }

    @Override
    public Void visitForStateChanged(StateChanged stateChanged, ViewAPI viewAPI){
        stateChanged.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForStartTurn(StartTurn startTurn, ViewAPI viewAPI){
        startTurn.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForErrorResponse(ErrorResponse errorResponse, ViewAPI viewAPI){
        errorResponse.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForCannonShotIncoming(CannonShotIncoming cannonShotIncoming, ViewAPI viewAPI){
        cannonShotIncoming.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForMeteorIncoming(MeteorIncoming meteorIncoming, ViewAPI viewAPI){
        meteorIncoming.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForPlayerLanded(PlayerLanded playerLanded, ViewAPI viewAPI){
        playerLanded.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForWrongUsername(WrongUsername wrongUsername, ViewAPI viewAPI){
        wrongUsername.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForAppropriateUsername(AppropriateUsername appropriateUsername, ViewAPI viewAPI){
        appropriateUsername.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForRequestNumPlayers(RequestNumPlayers requestNumPlayers, ViewAPI viewAPI){
        requestNumPlayers.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForIllegalTruck(IllegalTruck illegalTruck, ViewAPI viewAPI){
        illegalTruck.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForUpdateFromCard(UpdateFromCard updateFromCard, ViewAPI viewAPI){
        updateFromCard.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForSelectLevel(SelectLevel selectLevel, ViewAPI viewAPI){
        selectLevel.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForMatchFinished(MatchFinished matchFinished, ViewAPI viewAPI){
        matchFinished.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForLobbyUnavailable(LobbyUnavailable lobbyUnavailable, ViewAPI viewAPI){
        lobbyUnavailable.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForVisibleDeckResponse(VisibleDeckResponse visibleDeckResponse, ViewAPI viewAPI){
        visibleDeckResponse.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForIncorrectWelding(IncorrectWelding incorrectWelding, ViewAPI viewAPI){
        incorrectWelding.handle(viewAPI);
        return null;
    }

    @Override
    public Void visitForNewCardDrawn(NewCardDrawn newCardDrawn, ViewAPI viewAPI){
        newCardDrawn.handle(viewAPI);
        return null;
    }


}
