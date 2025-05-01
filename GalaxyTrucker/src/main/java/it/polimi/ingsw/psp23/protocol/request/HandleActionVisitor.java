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
}
