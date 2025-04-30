package it.polimi.ingsw.psp23.protocol.request;

public interface ActionVisitorSinglePar<T> {

    public Void visitForActivateCannon(ActivateCannon activateCannon);


    public Void visitForActivateEngine(ActivateEngine activateEngine);


    public Void visitForActivateShield(ActivateShield activateShield);


    public Void visitForAddTile(AddTile addTile, String username);


    public Void visitForDrawFromFaceUp(DrawFromFaceUp drawFromFaceUp);


    public Void visitForDrawFromHeap(DrawFromHeap drawFromHeap);


    public Void visitForReleaseTile(ReleaseTile releaseTile);


    public Void visitForRemoveTile(RemoveTile removeTile);


    public Void visitForRequestShip(RequestShip requestShip);


    public Void visitForRequestTileInfo(RequestTileInfo requestTileInfo);


    public Void visitForRequestUncovered(RequestUncovered requestUncovered);


    public Void visitForRotateTile(RotateTile rotateTile);


    public Void visitForSetCrew(SetCrew setCrew);


    public String visitForSetUsername(SetUsername setUsername);


    public Void visitForTurnHourGlass(TurnHourglass turnHourglass);

}
