package it.polimi.ingsw.psp23.protocol.request;

public interface ActionVisitor<T> {

    public T visitForActivateCannon(ActivateCannon activateCannon, String username);

    public T visitForActivateEngine(ActivateEngine activateEngine, String username);

    public T visitForActivateShield(ActivateShield activateShield, String username);

    public T visitForAddTile(AddTile addTile, String username);

    public T visitForDrawFromFaceUp(DrawFromFaceUp drawFromFaceUp, String username);

    public T visitForDrawFromHeap(DrawFromHeap drawFromHeap, String username);

    public T visitForReleaseTile(ReleaseTile releaseTile, String username);

    public T visitForRemoveTile(RemoveTile removeTile, String username);

    public T visitForRequestShip(RequestShip requestShip, String username);

    public T visitForRequestTileInfo(RequestTileInfo requestTileInfo, String username);

    public T visitForRequestUncovered(RequestUncovered requestUncovered, String username);

    public T visitForRotateTile(RotateTile rotateTile, String username);

    public T visitForSetCrew(SetCrew setCrew, String username);

    public T visitForSetUsername(SetUsername setUsername, String username);

    public T visitForTurnHourGlass(TurnHourglass turnHourglass, String username);

    public T visitForTakeReservedTile(TakeReservedTile takeReservedTile, String username);

    public T visitForReserveTile(ReserveTile reserveTile, String username);

    public T visitForRegisterNumPlayers(RegisterNumPlayers registerNumPlayers, String username);

    public T visitForNextTurn(NextTurn nextTurn, String username);
    
    public T visitForBuyShip(BuyShip buyShip, String username);

    public T visitForHelp(Help help, String username);

    public T visitForDockStation(DockStation dockStation, String username);

    public T visitForLoadGoods(LoadGood loadGood, String username);

    public T visitForReady(Ready ready, String username);

    public T visitForLandOnPlanet(Land land, String username);

    public T visitForReduceCrew(ReduceCrew reduceCrew, String username);

    public T visitForRemovePreciousItem(RemovePreciousItem removePreciousItem, String username);

    public T visitForTakeVisibleDeck(TakeVisibleDeck takeVisibleDeck, String username);

    public T visitForReleaseDeck(ReleaseDeck releaseDeck, String username);

    public T visitForFinished(Finished finished, String username);

    public T visitForPut(Put put, String username);

    public T visitForFixed(Fixed fixed, String username);

    public T visitForShowPlayersPositions(ShowPlayersPositions showPlayersPositions, String username);

    public T visitForLoseGood(LoseGood loseGood, String username);

    public T visitForMoveGood(MoveGood moveGood, String username);

    public T visitForRemoveBatteries(RemoveBatteries removeBatteries, String username);

}
