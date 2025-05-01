package it.polimi.ingsw.psp23.protocol.request;

public interface ActionVisitorSinglePar<T> {

    public T visitForActivateCannon(ActivateCannon activateCannon);


    public T visitForActivateEngine(ActivateEngine activateEngine);


    public T visitForActivateShield(ActivateShield activateShield);


    public T visitForAddTile(AddTile addTile, String username);


    public T visitForDrawFromFaceUp(DrawFromFaceUp drawFromFaceUp);


    public T visitForDrawFromHeap(DrawFromHeap drawFromHeap);


    public T visitForReleaseTile(ReleaseTile releaseTile);


    public T visitForRemoveTile(RemoveTile removeTile);


    public T visitForRequestShip(RequestShip requestShip);


    public T visitForRequestTileInfo(RequestTileInfo requestTileInfo);


    public T visitForRequestUncovered(RequestUncovered requestUncovered);


    public T visitForRotateTile(RotateTile rotateTile);


    public T visitForSetCrew(SetCrew setCrew);


    public T visitForSetUsername(SetUsername setUsername);


    public T visitForTurnHourGlass(TurnHourglass turnHourglass);


    public T visitForTakeReservedTile(TakeReservedTile takeReservedTile);


    public T visitForReserveTile(ReserveTile reserveTile);

}
