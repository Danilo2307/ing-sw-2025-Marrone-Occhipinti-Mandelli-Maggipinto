package it.polimi.ingsw.psp23.protocol.request;

public class SetUsernameActionVisitor implements ActionVisitorSinglePar<String>{

    @Override
    public String visitForActivateCannon(ActivateCannon activateCannon) {
        return null;
    }

    @Override
    public String visitForActivateEngine(ActivateEngine activateEngine) {
        return null;
    }

    @Override
    public String visitForActivateShield(ActivateShield activateShield) {
        return null;
    }

    @Override
    public String visitForAddTile(AddTile addTile) {
        return null;
    }

    @Override
    public String visitForDrawFromFaceUp(DrawFromFaceUp drawFromFaceUp) {
        return null;
    }

    @Override
    public String visitForDrawFromHeap(DrawFromHeap drawFromHeap) {
        return null;
    }

    @Override
    public String visitForReleaseTile(ReleaseTile releaseTile) {
        return null;
    }

    @Override
    public String visitForRemoveTile(RemoveTile removeTile) {
        return null;
    }

    @Override
    public String visitForRequestShip(RequestShip requestShip) {
        return null;
    }

    @Override
    public String visitForRequestTileInfo(RequestTileInfo requestTileInfo) {
        return null;
    }

    @Override
    public String visitForRequestUncovered(RequestUncovered requestUncovered) {
        return null;
    }

    @Override
    public String visitForRotateTile(RotateTile rotateTile) {
        return null;
    }

    @Override
    public String visitForSetCrew(SetCrew setCrew) {
        return null;
    }

    @Override
    public String visitForSetUsername(SetUsername setUsername){
        return setUsername.username();
    }

    @Override
    public String visitForTurnHourGlass(TurnHourglass turnHourglass) {
        return null;
    }

    @Override
    public String visitForTakeReservedTile(TakeReservedTile takeReservedTile) {
        return null;
    }

    @Override
    public String visitForReserveTile(ReserveTile reserveTile) {
        return null;
    }

}
