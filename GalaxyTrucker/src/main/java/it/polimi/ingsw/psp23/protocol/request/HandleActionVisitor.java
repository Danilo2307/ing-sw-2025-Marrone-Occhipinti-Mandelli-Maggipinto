package it.polimi.ingsw.psp23.protocol.request;

public class HandleActionVisitor implements ActionVisitor<Void> {

    @Override
    public Void visitForActivateCannon(ActivateCannon activateCannon, String username) {
        activateCannon.handle(username);
        return null;
    }

    @Override
    public Void visitForActivateEngine(ActivateEngine activateEngine, String username) {
        activateEngine.handle(username);
        return null;
    }

    @Override
    public Void visitForActivateShield(ActivateShield activateShield, String username) {
        activateShield.handle(username);
        return null;
    }

    @Override
    public Void visitForAddTile(AddTile addTile, String username) {
        addTile.handle(username);
        return null;
    }

    @Override
    public Void visitForDrawFromFaceUp(DrawFromFaceUp drawFromFaceUp, String username) {
        drawFromFaceUp.handle(username);
        return null;
    }

    @Override
    public Void visitForDrawFromHeap(DrawFromHeap drawFromHeap, String username) {
        drawFromHeap.handle(username);
        return null;
    }

    @Override
    public Void visitForReleaseTile(ReleaseTile releaseTile, String username) {
        releaseTile.handle(username);
        return null;
    }

    @Override
    public Void visitForRemoveTile(RemoveTile removeTile, String username) {
        removeTile.handle(username);
        return null;
    }

    @Override
    public Void visitForRequestShip(RequestShip requestShip, String username) {
        requestShip.handle(username);
        return null;
    }

    @Override
    public Void visitForRequestTileInfo(RequestTileInfo requestTileInfo, String username) {
        requestTileInfo.handle(username);
        return null;
    }

    @Override
    public Void visitForRequestUncovered(RequestUncovered requestUncovered, String username) {
        requestUncovered.handle(username);
        return null;
    }

    @Override
    public Void visitForRotateTile(RotateTile rotateTile, String username) {
        rotateTile.handle(username);
        return null;
    }

    @Override
    public Void visitForSetCrew(SetCrew setCrew, String username) {
        setCrew.handle(username);
        return null;
    }

    @Override
    public Void visitForSetUsername(SetUsername setUsername, String username) {
        setUsername.handle(username);
        return null;
    }

    @Override
    public Void visitForTurnHourGlass(TurnHourglass turnHourglass, String username) {
        turnHourglass.handle(username);
        return null;
    }

    @Override
    public Void visitForTakeReservedTile(TakeReservedTile takeReservedTile, String username) {
        takeReservedTile.handle(username);
        return null;
    }

    @Override
    public Void visitForReserveTile(ReserveTile reserveTile, String username) {
        reserveTile.handle(username);
        return null;
    }

    @Override
    public Void visitForRegisterNumPlayers(RegisterNumPlayers registerNumPlayers, String username) {
        registerNumPlayers.handle(username);
        return null;
    }

    @Override
    public Void visitForNextTurn(NextTurn nextTurn, String username) {
        nextTurn.handle(username);
        return null;
    }

    @Override
    public Void visitForBuyShip(BuyShip buyShip, String username) {
        buyShip.handle(username);
        return null;
    }

    @Override
    public Void visitForHelp(Help help, String username) {
        help.handle(username);
        return null;
    }

    @Override
    public Void visitForDockStation(DockStation dockStation, String username) {
        dockStation.handle(username);
        return null;
    }

    @Override
    public Void visitForLoadGoods(LoadGood loadGood, String username) {
        loadGood.handle(username);
        return null;
    }

    @Override
    public Void visitForReady(Ready ready, String username) {
        ready.handle(username);
        return null;
    }

    @Override
    public Void visitForLandOnPlanet(Land land, String username) {
        land.handle(username);
        return null;
    }

    @Override
    public Void visitForReduceCrew(ReduceCrew reduceCrew, String username) {
        reduceCrew.handle(username);
        return null;
    }

    @Override
    public Void visitForRemovePreciousItem(RemovePreciousItem removePreciousItem, String username) {
        removePreciousItem.handle(username);
        return null;
    }

    @Override
    public Void visitForFinished(Finished finished, String username) {
        finished.handle(username);
        return null;
    }

    @Override
    public Void visitForTakeVisibleDeck(TakeVisibleDeck takeVisibleDeck, String username) {
        takeVisibleDeck.handle(username);
        return null;
    }

    @Override
    public Void visitForReleaseDeck(ReleaseDeck releaseDeck, String username) {
        releaseDeck.handle(username);
        return null;
    }

    @Override
    public Void visitForPut(Put put, String username) {
        put.handle(username);
        return null;
    }

    @Override
    public Void visitForFixed(Fixed fixed, String username) {
        fixed.handle(username);
        return null;
    }

    @Override
    public Void visitForShowPlayersPositions(ShowPlayersPositions showPlayersPositions, String username) {
        showPlayersPositions.handle(username);
        return null;
    }

    @Override
    public Void visitForLoseGood(LoseGood loseGood, String username) {
        loseGood.handle(username);
        return null;
    }

    @Override
    public Void visitForMoveGood(MoveGood moveGood, String username) {
        moveGood.handle(username);
        return null;
    }

    @Override
    public Void visitForRemoveBatteries(RemoveBatteries removeBatteries, String username) {
        removeBatteries.handle(username);
        return null;
    }

    @Override
    public Void visitForLeaveFlight(LeaveFlight leaveFlight, String username){
        leaveFlight.handle(username);
        return null;
    }

    @Override
    public Void visitForDrawCard(DrawCard drawCard, String username){
        drawCard.handle(username);
        return null;
    }

    @Override
    public Void visitForCreateNewMatch(CreateNewMatch createNewMatch, String username){
        createNewMatch.handle(username);
        return null;
    }

    @Override
    public Void visitForAccessMatch(AccessMatch accessMatch, String username){
        accessMatch.handle(username);
        return null;
    }

}
