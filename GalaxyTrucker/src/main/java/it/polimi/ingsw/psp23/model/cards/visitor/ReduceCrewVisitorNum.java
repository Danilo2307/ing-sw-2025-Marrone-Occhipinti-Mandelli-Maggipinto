package it.polimi.ingsw.psp23.model.cards.visitor;

import it.polimi.ingsw.psp23.model.cards.*;

public class ReduceCrewVisitorNum implements VisitorCoordinateNum<Void> {

    @Override
    public Void visitForAbandonedShip(AbandonedShip abandonedShip, String username, int i, int j, int num){
        abandonedShip.reduceCrew(username, i, j, num);
        return null;
    }

    @Override
    public Void visitForCombatZone(CombatZone combatZone, String username, int i, int j, int num){
        combatZone.reduceCrew(username, i, j, num);
        return null;
    }

    @Override
    public Void visitForSlavers(Slavers slavers, String username, int i, int j, int num){
        slavers.reduceCrew(username, i, j, num);
        return null;
    }

    @Override
    public Void visitForSmugglers(Smugglers smugglers, String username, int i, int j, int num){
        return null;
    }

}
