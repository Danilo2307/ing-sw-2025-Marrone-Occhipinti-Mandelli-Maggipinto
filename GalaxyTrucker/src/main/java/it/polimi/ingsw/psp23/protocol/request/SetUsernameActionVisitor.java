package it.polimi.ingsw.psp23.protocol.request;

public class SetUsernameActionVisitor implements ActionVisitorSinglePar<String>{

    @Override
    public Void visitForActivateCannon(ActivateCannon activateCannon) {
        return null;
    }

    @Override
    public Void visitForActivateEngine(ActivateEngine activateEngine) {
        return null;
    }

    @Override
    public Void visitForActivateShield(ActivateShield activateShield) {
        return null;
    }

    @Override
    public Void visitForAddTile(AddTile addTile, String username) {
        return null;
    }

    @Override
    public Void visitForDrawFromFaceUp(DrawFromFaceUp drawFromFaceUp) {
        return null;
    }

    @Override
    public Void visitForDrawFromHeap(DrawFromHeap drawFromHeap) {
        return null;
    }

    @Override
    public Void visitForReleaseTile(ReleaseTile releaseTile) {
        return null;
    }

    @Override
    public Void visitForRemoveTile(RemoveTile removeTile) {
        return null;
    }

    @Override
    public Void visitForRequestShip(RequestShip requestShip) {
        return null;
    }

    @Override
    public Void visitForRequestTileInfo(RequestTileInfo requestTileInfo) {
        return null;
    }

    @Override
    public Void visitForRequestUncovered(RequestUncovered requestUncovered) {
        return null;
    }

    @Override
    public Void visitForRotateTile(RotateTile rotateTile) {
        return null;
    }

    @Override
    public Void visitForSetCrew(SetCrew setCrew) {
        return null;
    }

    @Override
    public String visitForSetUsername(SetUsername setUsername){
        return setUsername.username();
    }

    @Override
    public Void visitForTurnHourGlass(TurnHourglass turnHourglass) {
        return null;
    }

    @Override
    public Void visitForTakeReservedTile(TakeReservedTile takeReservedTile) {
        return null;
    }

    @Override
    public Void visitForReserveTile(ReserveTile reserveTile) {
        return null;
    }

}
