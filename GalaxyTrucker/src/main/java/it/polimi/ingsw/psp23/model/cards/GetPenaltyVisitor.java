package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.model.enumeration.Challenge;

public class GetPenaltyVisitor implements VisitorParametrico{

    public Void visitForPlanets(Planets planets, int index) {
        return null;
    }

    public Challenge visitForCombatZone(CombatZone combatZone, int index) {
        return combatZone.getPenalties().get(index - 1);
    }

}
