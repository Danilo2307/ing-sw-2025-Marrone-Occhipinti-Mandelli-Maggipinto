package it.polimi.ingsw.psp23.protocol.request;

public interface ActionVisitor<T> {

    public T visitForActivateCannon(ActivateCannon activateCannon, String username);

    public T visitForActivateEngine(ActivateEngine activateEngine, String username);

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
}
