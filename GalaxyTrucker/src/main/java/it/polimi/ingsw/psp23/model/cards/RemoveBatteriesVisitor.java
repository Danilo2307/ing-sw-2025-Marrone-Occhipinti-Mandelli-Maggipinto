package it.polimi.ingsw.psp23.model.cards;

public class RemoveBatteriesVisitor implements VisitorCoordinateNum<Void> {

    public Void visitForAbandonedShip(AbandonedShip abandonedShip, String username, int i, int j, int num){
        return null;
    }

    public Void visitForCombatZone(CombatZone combatZone, String username, int i, int j, int num){
        combatZone.removeBatteries(username, i, j, num);
        return null;
    }

    public Void visitForSlavers(Slavers slavers, String username, int i, int j, int num){
        return null;
    }

    public Void visitForSmugglers(Smugglers smugglers, String username, int i, int j, int num){
        smugglers.removeBatteries(username, i, j, num);
        return null;
    }
}
